import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginRequest } from 'src/app/modals/login-request';
import { LoginResponse } from 'src/app/modals/login-response';
import { AuthService } from 'src/app/services/auth.service';
import { IntegrationService } from 'src/app/services/integration.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  userForm: FormGroup;
  errorMessage: string = '';
  loginRequest: LoginRequest = new LoginRequest();

  constructor(private fb: FormBuilder, private integrationService: IntegrationService, private router: Router, private authService: AuthService) {
    this.userForm = this.fb.group({
      userEmail: ['', Validators.required],
      userPassword: ['', [Validators.required, Validators.minLength(3)]],
    });
  }


  doLogin() {
    if (this.userForm.invalid) {
      alert("Please fill in the required fields correctly.");
      return;
    }
    localStorage.removeItem('token');

    this.loginRequest.userEmail = this.userForm.value.userEmail;
    this.loginRequest.userPassword = this.userForm.value.userPassword;

    this.integrationService.doLogin(this.loginRequest).subscribe({

      next: (response: LoginResponse) => {
        console.log("Received Response:", response); // Log the full response
        const token = response.token; // Extract the token from the response
        // console.log("Received Token:", token);
        // Store the token in localStorage or sessionStorage
        localStorage.setItem('token', token);
        // Example of storing token
        const role: string = this.authService.getUserRole();
        if (role == 'ROLE_STUDENT') {
          this.router.navigate(['/student'])
        } else if (role == 'ROLE_INSTRUCTOR') {
          this.router.navigate(['/instructor'])
        }else if (role == 'ROLE_ADMIN') {
          this.router.navigate(['/admin'])
        }
      },
      error: (err) => {
        if (err.status == 401||409) {
          this.errorMessage = "Invalid Credentials. Please try again!";
        }
        console.error("Error message received", err);
        // Handle error response, show an appropriate message to the user
      }
    });
  }
}
