import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from '../models/login-request';
import { Observable } from 'rxjs';
import { LoginResponse } from '../models/login-response';
const API_URL = "http://localhost:8080/internship-portal/api/v1/login";
@Injectable({
  providedIn: 'root'
})
export class IntegrationService {

  constructor(private http: HttpClient) {

  }
  doLogin(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(API_URL, request);
  }
}
