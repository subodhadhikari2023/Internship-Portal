import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Route, Router } from '@angular/router';
import { ForgetPasswordService } from 'src/app/services/forget-password.service';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.css']
})
export class ForgetPasswordComponent implements OnInit {



  forgetForm: FormGroup;
  otpForm: FormGroup;
  accountNotFound: boolean = false;
  accountFound: boolean = false;
  accountClaimed: boolean = false;
  email: any = undefined;
  isOtpVerified: boolean = false;
  otpInvalid: boolean = false;
  newPasswordForm: FormGroup;
  constructor(private fb: FormBuilder, private resetPasswordService: ForgetPasswordService, private router: Router) {
    this.forgetForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
    this.otpForm = this.fb.group({
      otp: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]]

    });
    this.newPasswordForm = this.fb.group({
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/)
      ]]

    });
  }
  ngOnInit(): void {
  }

  onSubmit() {
    const email = this.forgetForm.value.email;


    this.resetPasswordService.findAccount(email).subscribe({
      next: (res) => {
        if (!res) {
          this.accountNotFound = true;
          this.accountFound = false;

        } else {
          this.accountFound = true;
          localStorage.removeItem('email');
          localStorage.setItem('email', res.userEmail);
          console.log(res.userEmail);

          this.accountNotFound = false;
          this.email = localStorage.getItem('email');
        }
      }
    })


  }

  claimAccount() {
    this.accountClaimed = true;
    this.resetPasswordService.getOTPForRegisteredAccount().subscribe({
      next: (res) => {
        console.log(res.oneTimePassword);

      }, error: () => {
        console.log("error occured");

      }
    })
  }
  submitOtp() {
    const request = {
      oneTimePassword: this.otpForm.value.otp,
      userEmail: localStorage.getItem('email')
    };

    this.resetPasswordService.submitOTP(request).subscribe({
      next: (res) => {
        if (res) {
          this.otpInvalid = false; 
          this.isOtpVerified=true;


        }
      }, error: (err) => {
        console.error(err);
        this.isOtpVerified = false;
        this.otpInvalid = true; 


      }
    })
  }

  resetPassword() {
    const password = this.newPasswordForm.value.password;
    this.resetPasswordService.resetPassword(password).subscribe({
      next: (res) => {
        console.log("password reset success!");
        this.router.navigate(['login']);

      }, error: (err) => {
        console.error(err);

      }
    })
  }
}
