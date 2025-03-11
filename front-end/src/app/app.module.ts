import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { LoginComponent } from './components/login/login.component';
import { StudentsHomeComponent } from './components/students-home/students-home.component';
import { LandingComponent } from './components/landing/landing.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { OtpComponent } from './components/otp/otp.component';
import { VerifyComponent } from './components/verify/verify.component';
import { InstructorsHomeComponent } from './components/instructors-home/instructors-home.component';
import { FooterComponent } from './components/footer/footer.component';
import { CreateInternshipComponent } from './components/create-internship/create-internship.component';
import { PopupComponent } from './components/popup/popup.component';
import { ViewInternshipsComponent } from './components/view-internships/view-internships.component';
import { EditInternshipComponent } from './components/edit-internship/edit-internship.component';
import { StudentNavComponent } from './components/student-nav/student-nav.component';
import { ViewApplicationComponent } from './components/view-application/view-application.component';
import { ViewApplicationModalComponent } from './components/view-application-modal/view-application-modal.component';

// Interceptors
import { CustomInterceptor } from './interceptors/custom.interceptor';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    StudentsHomeComponent,
    LandingComponent,
    RegistrationComponent,
    OtpComponent,
    VerifyComponent,
    InstructorsHomeComponent,
    FooterComponent,
    CreateInternshipComponent,
    PopupComponent,
    ViewInternshipsComponent,
    EditInternshipComponent,
    StudentNavComponent,
    ViewApplicationComponent,
    ViewApplicationModalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule, // ✅ Added this
    MatDialogModule, // ✅ Added this
    MatButtonModule // ✅ Added this
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CustomInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
