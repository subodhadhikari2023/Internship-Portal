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



const routes: Routes = [

  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'student', component: StudentsHomeComponent, canActivate: [AuthGuard], data: { role: 'ROLE_STUDENT' } },
  { path: 'student/view-applications', component: ViewApplicationComponent, canActivate: [AuthGuard], data: { role: 'ROLE_STUDENT' } },
  { path: 'instructor', component: InstructorsHomeComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'instructor/create-internship', component: CreateInternshipComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' }, pathMatch: 'full' },
  { path: 'instructor/view-internships', component: ViewInternshipsComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'instructor/manage-internships', component: EditInternshipComponent, canActivate: [AuthGuard], data: { role: 'ROLE_INSTRUCTOR' } },
  { path: 'register', component: RegistrationComponent },
  { path: 'verifyOTP', component: OtpComponent },
  { path: 'verify', component: VerifyComponent },
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
