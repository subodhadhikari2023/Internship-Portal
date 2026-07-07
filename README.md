# Internship Portal for Government of Sikkim

[![Contributors][contributors-shield]][contributors-url]
[![Issues][issues-shield]][issues-url]
[![GNU License][license-shield]][license-url]
-----------------------------------------------
## Highlight

Production system delivered for the Department of IT, Government of Sikkim.

## 🔥 Contributors

**Subodh Adhikari**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/subodh-adhikari-4b811a296/)
[![Outlook](https://img.shields.io/badge/Outlook-blue?style=flat-square&logo=microsoft-outlook)](mailto:subodhadhikari2023@outlook.com)
[![GitHub](https://img.shields.io/badge/GitHub-black?style=flat-square&logo=github)](https://github.com/subodhadhikari2023/)

---

## 📖 Overview

The **Internship Portal** is a web-based platform developed to digitize the internship application and management process for students applying under the **Government of Sikkim, Department of Information Technology**. It provides students and administrators with tools for application submission, project file storage, status tracking, and certificate issuance.

## 🎯 Features

### **For Students/Interns**
- Online application for internships.
- Application status tracking.
- Upload and manage project files in a centralized repository.
- Request and claim internship completion certificates.
- Receive real-time email notifications about application progress.

### **For Administrators**
- Manage and approve internship applications.
- Track and monitor internship progress.
- Generate and issue completion certificates.
- Provide real-time updates to students.

## Tech Stack

<div align="center">

**Frontend**

![Angular 14](https://img.shields.io/badge/Angular_14-DD0031?style=for-the-badge&logo=angular&logoColor=white) ![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white) ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)

**Backend**

![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![Hibernate JPA](https://img.shields.io/badge/Hibernate_JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

**Database**

![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

**DevOps & Tools**

![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![Docker Compose](https://img.shields.io/badge/Docker_Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white) ![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white) ![VS Code](https://img.shields.io/badge/VS_Code-007ACC?style=for-the-badge&logo=visual-studio-code&logoColor=white)

</div>

<!-- Portfolio sync reads the bullet list below -->
- Angular 14
- TypeScript
- Spring Boot
- Spring Security
- JWT
- Hibernate/JPA
- MySQL
- Spring MVC
- REST API
- Docker
- Docker Compose
- Maven
- IntelliJ IDEA
- VS Code
- Git
- HTML/CSS
- Postman

## 🔍 Problem Statement

Currently, the **internship cycle is managed manually** using offline methods, leading to:
- Delayed application processing.
- No centralized repository for storing and sharing project files.
- Difficulty in tracking and managing different stages of the internship.
- Time-consuming manual certification process.
- Lack of real-time updates and notifications.

## 💡 Solution Approach

The **Internship Portal** addresses these issues by:
- Providing a **web-based registration system** for students.
- Implementing a **centralized project file repository** with role-based access control.
- Automating the **certificate issuance** process.
- Enabling **email notifications** for important updates.
- Offering a **dashboard for administrators** to manage and track internships efficiently.

## 🛠 Installation Guide

### **Prerequisites**
- **Java 17** installed
- **Node.js & npm** installed
- **MySQL 8.0.33** installed
- **Git** installed

### **Steps to Run the Project Locally**
1. **Clone the Repository**
   ```sh
   git clone https://github.com/subodhadhikari2023/Internship-Portal.git
   cd Internship-Portal
   ```

2. **Backend Setup (Spring Boot)**
   ```sh
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. **Frontend Setup (Angular)**
   ```sh
   cd frontend
   npm install
   ng serve
   ```

4. **Database Setup**
   - Create a MySQL database named `internship_portal`
   - Configure database credentials in `application.properties`

5. **Access the Application**
   - **Frontend:** `http://localhost:4200`
   - **Backend API:** `http://localhost:8080/internship-portal/api/v1`



## Screenshots

![Admin Dashboard](docs/screenshots/admin/admin-dashboard.png)
![Student Dashboard](docs/screenshots/student/dashboard-student.png)
![Instructor Dashboard](docs/screenshots/instructor/dashboard-instructor.png)

## 📜 License

Distributed under the **MIT License**. See LICENSE for more information.

## 📬 Contact

For any queries or suggestions, feel free to contact:
- **Email:** subodhadhikari2023@outlook.com
- **GitHub Issues:** [Open an Issue](https://github.com/subodhadhikari2023/Internship-Portal/issues)

---

[contributors-shield]: https://img.shields.io/github/contributors/subodhadhikari2023/Internship-Portal?style=for-the-badge
[contributors-url]: https://github.com/subodhadhikari2023/Internship-Portal/graphs/contributors
[issues-shield]: https://img.shields.io/github/issues/subodhadhikari2023/Internship-Portal?style=for-the-badge
[issues-url]: https://github.com/subodhadhikari2023/Internship-Portal/issues
[license-shield]: https://img.shields.io/badge/License-GPLv3-blue.svg
[license-url]: https://github.com/subodhadhikari2023/Internship-Portal/blob/main/LICENSE
