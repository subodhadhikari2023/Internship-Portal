import { Injectable } from '@angular/core';
import { RegistrationRequest } from '../models/registration-request';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  private registeredEmail: string = '';
  private baseUrl = 'http://localhost:8080/internship-portal/api/v1';

  constructor(private http: HttpClient) { }

  registerUser(registrationRequest: RegistrationRequest): Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.baseUrl}/register`, registrationRequest, { observe: 'response' });
  }
  setRegisteredEmail(email: string): void {
    this.registeredEmail = email;
  }


  getRegisteredEmail(): string {
    return this.registeredEmail;
  }
}
