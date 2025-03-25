import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { IntegrationService } from 'src/app/services/integration.service';
import { RegistrationService } from 'src/app/services/registration.service';

@Component({
  selector: 'app-otp',
  templateUrl: './otp.component.html',
  styleUrls: ['./otp.component.css']
})
export class OtpComponent implements OnInit {
  otpForm!: FormGroup; // Use the non-initialized form group
  userEmail: string = '';
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private registrationService: RegistrationService,
    private authService: IntegrationService
  ) { }

  ngOnInit(): void {
    // Initialize the form with validation
    this.otpForm = this.fb.group({
      otp: ['', [Validators.required, Validators.pattern("^[0-9]{6}$")]]  // Assuming OTP is a 6-digit number
    });

    // Fetch the email from the RegistrationService
    this.userEmail = this.registrationService.getRegisteredEmail();

    // Check if the email is found; if not, redirect to registration
    if (!this.userEmail) {
      this.errorMessage = "No registered email found. Please register again.";
      console.error(this.errorMessage);
      this.router.navigate(['/register']);  // Redirect back to registration if no email is found
    }
  }

  onSubmit(): void {
    if (this.otpForm.invalid) {
      this.otpForm.markAllAsTouched();
      return;
    }

    const otp = this.otpForm.value.otp;

    this.authService.verifyOtp(this.userEmail, otp).subscribe({
      next: () => {
        console.log('OTP verified successfully');
        this.router.navigate(['/login']);
      },
      error: (error: any) => {
        console.error('Error verifying OTP:', error);
        this.errorMessage = "OTP verification failed. Please try again.";
      }
    });
  }

}
