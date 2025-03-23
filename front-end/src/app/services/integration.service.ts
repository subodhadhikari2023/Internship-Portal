import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from '../modals/login-request';
import { Observable } from 'rxjs';
import { LoginResponse } from '../modals/login-response';
const BASE_URL = "http://127.0.0.1:8080/internship-portal/api/v1/";
@Injectable({
  providedIn: 'root'
})
export class IntegrationService {

  constructor(private http: HttpClient) {

  }
  doLogin(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${BASE_URL}login`, request);
  }
  accessMessage() {
    return this.http.get<LoginResponse>(`${BASE_URL}students/hello`);
  }
}
