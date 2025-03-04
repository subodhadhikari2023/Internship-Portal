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
    EditInternshipComponent
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
