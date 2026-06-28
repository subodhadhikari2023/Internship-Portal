import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { StudentsHomeComponent } from './components/students-home/students-home.component';
import { LandingComponent } from './components/landing/landing.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { OtpComponent } from './components/otp/otp.component';
import { VerifyComponent } from './components/verify/verify.component';
import { CustomInterceptor } from './interceptors/custom.interceptor';
import { InstructorsHomeComponent } from './components/instructors-home/instructors-home.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FooterComponent } from './components/footer/footer.component';
import { CreateInternshipComponent } from './components/create-internship/create-internship.component';
import { PopupComponent } from './components/popup/popup.component';
import { ViewInternshipsComponent } from './components/view-internships/view-internships.component';
import { EditInternshipComponent } from './components/edit-internship/edit-internship.component';
import { StudentNavComponent } from './components/student-nav/student-nav.component';
import { ViewApplicationComponent } from './components/view-application/view-application.component';
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
import { AdminNavbarComponent } from './components/admin-navbar/admin-navbar.component';
import { UpdateAdminComponent } from './components/update-admin/update-admin.component';
import { StudentFooterComponent } from './components/student-footer/student-footer.component';




@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    StudentsHomeComponent,
    LandingComponent,
    RegistrationComponent,
    OtpComponent,
    VerifyComponent,
    InstructorsHomeComponent,
    NavbarComponent,
    FooterComponent,
    CreateInternshipComponent,
    PopupComponent,
    ViewInternshipsComponent,
    EditInternshipComponent,
    StudentNavComponent,
    ViewApplicationComponent,
    ViewApplicationsComponent,
    ViewInternshipStudentComponent,
    CreateProjectComponent,
    ViewEnrolledStudentsComponent,
    ProjectDetailsComponent,
    ViewProjectDetailsStudentComponent,
    UpdateProfileStudentComponent,
    ViewStudentDetailsForInstructorComponent,
    UpdateProfileInstructorComponent,
    AdminHomeComponent,
    ForgetPasswordComponent,
    AdminNavbarComponent,
    UpdateAdminComponent,
    StudentFooterComponent,
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule


  ],
  providers: [{
    provide: HTTP_INTERCEPTORS, useClass: CustomInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
