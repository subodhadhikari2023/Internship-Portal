import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { StudentsHomeComponent } from './components/students-home/students-home.component';
import { LandingComponent } from './components/landing/landing.component';
import { OtpComponent } from './components/otp/otp.component';
import { VerifyComponent } from './components/verify/verify.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { AuthGuard } from './guards/auth.guard';
import { InstructorsHomeComponent } from './components/instructors-home/instructors-home.component';
import { CreateInternshipComponent } from './components/create-internship/create-internship.component';
import { ViewInternshipsComponent } from './components/view-internships/view-internships.component';
import { EditInternshipComponent } from './components/edit-internship/edit-internship.component';
import {ViewApplicationComponent} from "./components/view-application/view-application.component";
import { ViewApplicationsComponent } from './components/view-applications/view-applications.component';
import { ViewInternshipStudentComponent } from './components/view-internship-student/view-internship-student.component';
import { CreateProjectComponent } from './components/create-project/create-project.component';
import { ViewEnrolledStudentsComponent } from './components/view-enrolled-students/view-enrolled-students.component';
import { ProjectDetailsComponent } from './components/project-details/project-details.component';
import { ViewProjectDetailsStudentComponent } from './components/view-project-details-student/view-project-details-student.component';
import { UpdateProfileStudentComponent } from './components/update-profile-student/update-profile-student.component';
import { ViewStudentDetailsForInstructorComponent } from './components/view-student-details-for-instructor/view-student-details-for-instructor.component';
import { UpdateProfileInstructorComponent } from './components/update-profile-instructor/update-profile-instructor.component';
import { AdminHomeComponent } from './components/admin-home/admin-home.component';
import { ForgetPasswordComponent } from './components/forget-password/forget-password.component';
import { UpdateAdminComponent } from './components/update-admin/update-admin.component';



const routes: Routes = [

  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'student', component: StudentsHomeComponent, canActivate: [AuthGuard], data: { role: 'ROLE_STUDENT' } },
  { path: 'admin', component: AdminHomeComponent, canActivate: [AuthGuard], data: { role: 'ROLE_ADMIN' } },
  { path: 'admin/update-profile', component: UpdateAdminComponent, canActivate: [AuthGuard], data: { role: 'ROLE_ADMIN' } },
  { path: 'student/view-applications', component: ViewApplicationComponent, canActivate: [AuthGuard], data: { role: 'ROLE_STUDENT' } },
  { path: 'student/update-profile', component: UpdateProfileStudentComponent, canActivate: [AuthGuard], data: { role: 'ROLE_STUDENT' } },
  { path: 'student/view-internships', component: ViewInternshipStudentComponent, canActivate: [AuthGuard], data: { role: 'ROLE_STUDENT' } },
  { path: 'student/project-details/:id', component: ViewProjectDetailsStudentComponent, canActivate: [AuthGuard], data: { role: 'ROLE_STUDENT' } },
  { path: 'instructor', component: InstructorsHomeComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'instructor/create-internship', component: CreateInternshipComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' }, pathMatch: 'full' },
  { path: 'instructor/view-internships', component: ViewInternshipsComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'instructor/update-profile', component: UpdateProfileInstructorComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'instructor/manage-internships', component: EditInternshipComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'instructor/applications', component: ViewApplicationsComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'instructor/create-project', component: CreateProjectComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'instructor/view-internship-students', component: ViewEnrolledStudentsComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'instructor/project-details/:id', component: ProjectDetailsComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'instructor/student-details/:id', component: ViewStudentDetailsForInstructorComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'register', component: RegistrationComponent },
  { path: 'verifyOTP', component: OtpComponent },
  { path: 'verify', component: VerifyComponent },
  { path: 'forget-password', component: ForgetPasswordComponent },
  
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
