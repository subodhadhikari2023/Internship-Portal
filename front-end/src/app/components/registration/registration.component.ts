import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RegistrationRequest } from 'src/app/modals/registration-request';
import { RegistrationService } from 'src/app/services/registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  userForm!: FormGroup;
  registrationRequest: RegistrationRequest = new RegistrationRequest();
  errorMessage: string | null = null;

  constructor(private fb: FormBuilder, private registrationService: RegistrationService, private router: Router) { }

  ngOnInit(): void {
    this.userForm = this.fb.group({
      userEmail: ['', [Validators.required, Validators.email]],
      userPassword: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/)
      ]],
      userName: ['', Validators.required],
      userPhoneNumber: ['', [
        Validators.required,
        Validators.pattern("^[0-9]{10}$"),
        Validators.minLength(10)
      ]],
    });
  }

  doRegistration() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    this.registrationRequest.userEmail = this.userForm.get('userEmail')?.value;
    this.registrationRequest.userPassword = this.userForm.get('userPassword')?.value;
    this.registrationRequest.userName = this.userForm.get('userName')?.value;
    this.registrationRequest.userPhoneNumber = this.userForm.get('userPhoneNumber')?.value;

    this.registrationService.registerUser(this.registrationRequest).subscribe({
      next: () => {
        this.registrationService.setRegisteredEmail(this.registrationRequest.userEmail);
        this.router.navigate(['/verifyOTP']);
        
      },
      error: (err) => {
        console.error("Error message received", err);
        if (err.status === 409) {
          this.errorMessage = "Email already registered. Please use a different email.";

        } else {
          this.errorMessage = "Registration Failed. Please try again.";

        }
      }
    });
  }
}
