import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from '../models/login-request';
import { Observable } from 'rxjs';
import { LoginResponse } from '../models/login-response';
import { environment } from 'src/environments/environment';

const BASE_URL = `${environment.apiBaseUrl}/public/`;
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


  verifyOtp(email: string, otp: string): Observable<any> {
    return this.http.post<any>(`${BASE_URL}register/verify?email=${email}&otp=${otp}`, {});
  }
}
