import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginRequest } from 'src/app/models/login-request';
import { LoginResponse } from 'src/app/models/login-response';
import { IntegrationService } from 'src/app/services/integration.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  userForm: FormGroup;
  loginRequest: LoginRequest = new LoginRequest();

  constructor(private fb: FormBuilder, private integrationService: IntegrationService,private router:Router) {
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

    this.loginRequest.userEmail = this.userForm.value.userEmail;
    this.loginRequest.userPassword = this.userForm.value.userPassword;

    this.integrationService.doLogin(this.loginRequest).subscribe({
      next: (response: LoginResponse) => {
        console.log("Received Response:", response); // Log the full response
        const token = response.token; // Extract the token from the response
        console.log("Received Token:", token);
        // Store the token in localStorage or sessionStorage
        localStorage.setItem('token', token); // Example of storing token
        this.router.navigate(['/home'])
      },
      error: (err) => {
        console.error("Error message received", err);
        // Handle error response, show an appropriate message to the user
      }
    });
  }
}
