# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Purpose

Internship Portal for the Government of Sikkim, Department of Information Technology. Digitizes the full internship lifecycle: student registration → application → project assignment → certificate issuance. Previously managed entirely offline.

## Repository Layout

```
frontend/   → Angular 14 SPA
backend/    → Spring Boot 3.4.2 REST API (Java 17, Maven)
```

## Commands

### Backend (from `backend/`)

```bash
./mvnw spring-boot:run          # Run the API server (port 8080)
./mvnw clean install            # Build and run all tests
./mvnw test                     # Run tests only
./mvnw test -Dtest=SomeTest     # Run a single test class
./mvnw package -DskipTests      # Build JAR without tests
```

The API is served at `http://localhost:8080/internship-portal`.

All config is injected via environment variables (see `application.properties`). Required vars before running:
`DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `JWT_SECRET`, `SPRING_MAIL_HOST`, `SPRING_MAIL_PORT`, `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD`, `MAIL_HOST_NAME`, `ROOT` (file storage root), `URLPREFIX`, `ADMIN_USERNAME`, `ADMIN_PASSWORD`, `ADMIN_EMAIL`, `ADMIN_PHONE_NUMBER`, `ORGANIZATIONLOGOPATH`, `OFFICIALSEALPATH`, `INSTRUCTORSIGNATUREPATH`, `DIRECTORSIGNATUREPATH`, `CERTIFICATETEMPLATEPATH`.

### Frontend (from `frontend/`)

```bash
npm install         # Install dependencies
ng serve            # Dev server at http://localhost:4200
ng build            # Production build
ng test             # Run Karma/Jasmine unit tests
```

## Architecture

### Security model

Three roles enforced at both tiers:

| Role | Spring authority | Angular routes |
|---|---|---|
| Admin | `ROLE_ADMIN` | `/admin/**` |
| Instructor | `ROLE_INSTRUCTOR` | `/instructor/**` |
| Student | `ROLE_STUDENT` | `/student/**` |

**Backend:** `SecurityConfiguration` restricts API namespaces by role. Session is stateless (JWT only). `JwtFilter` validates the token on every request. Public endpoints live under `/api/v1/public/**` and require no auth.

**Frontend:** `AuthGuard` decodes the JWT from `localStorage` (via `jwtDecode`) and checks `token.role` against the route's `data.role`. `CustomInterceptor` attaches `Authorization: Bearer <token>` to every non-login/register request.

### Backend layer pattern

```
Controller → Service interface → ServiceImpl → Repository (JpaRepository)
```

- Controllers are in `controllers/` — one per role: `PublicController`, `CommonController`, `StudentController`, `InstructorController`, `AdminController`.
- Service interfaces are in `services/`; implementations are in `services/Implementation/`.
- API responses are always wrapped in `wrapper/Response<T>`.
- DTOs/wrappers (never entities directly) are returned to clients — see `wrapper/` for `StudentWrapper`, `InternshipWrapper`, `ApplicationWrapper`, etc.
- `wrapper/APIRequest<T>` is the standard single-value request body shape.

### Key domain relationships

- `Users` → `Roles[]` (eager), `Education` (one-to-one, polymorphic subclasses), `DepartmentDetails` (many-to-one), `InternshipStudents[]` (join between student and internship), `Project[]`.
- `Internship` → `createdBy` (Users/instructor), `DepartmentDetails`, `requiredSkills` (ElementCollection), `Application[]`.
- `Certificate` → `InternshipStudents` (one-to-one); only issued when `StudentInternshipStatus == COMPLETED` and no prior certificate exists.
- `OneTimePassword` is used for both registration verification and password reset.

### Email notifications

`MailServiceImpl` is annotated `@Async("taskExecutor")` — all email is sent off the request thread. Triggered events: registration OTP, application accept/reject, instructor account creation, project allocation, project status change, password reset OTP.

### File storage

All uploaded files (profile pictures, resumes, project files, certificates) are written to the local filesystem under the `ROOT` env var. The directory structure is: `ROOT/{department}/{internship}/{studentEmail}/`. Files are served back via `/api/v1/public/download?filePath=...` as `Resource` responses.

### Certificate generation

`CertificateServiceImpl.createCertificate()` guards against duplicates and incomplete internships, then calls the static `createPDFCertificate()` which uses iTextPDF 5.5 to build a landscape A4 PDF with logo, seal, and signatures from filesystem paths configured via env vars.

### Admin auto-seeding

`AdminInitializer` (`utils/AdminInitializer.java`) runs on startup and creates exactly one admin user from `ADMIN_*` env vars if no admin exists yet.

### Frontend service layer

Angular services (`services/`) call the backend and return `Observable`s. The `integration.service.ts` handles cross-cutting API calls. Components consume services directly — no NgRx or other state management library is used.
