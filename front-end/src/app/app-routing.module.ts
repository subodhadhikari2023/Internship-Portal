import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { StudentsHomeComponent } from './components/students-home/students-home.component';
import { LandingComponent } from './components/landing/landing.component';
import { OtpComponent } from './components/otp/otp.component';
import { VerifyComponent } from './components/verify/verify.component';
import { RegistrationComponent } from './components/registration/registration.component';


const routes: Routes = [

  { path: '', component: LandingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: StudentsHomeComponent },
  { path: 'register', component: RegistrationComponent },
  { path: 'verifyOTP', component: OtpComponent },
  { path: 'verify', component: VerifyComponent }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
