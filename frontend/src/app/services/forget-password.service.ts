import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, retry } from 'rxjs';


const BASE_URL = "http://127.0.0.1:8080/internship-portal/api/v1/";

@Injectable({
  providedIn: 'root'
})
export class ForgetPasswordService {

  constructor(private http: HttpClient) { }

  findAccount(email: string) {
    return this.http.get<any>(`${BASE_URL}public/forget-password?email=${email}`).pipe(
      map(res => res.entity)
    );
  }

  getOTPForRegisteredAccount() {
    localStorage.getItem('email');
    return this.http.get<any>(`${BASE_URL}public/get-password-change-otp?email=${localStorage.getItem('email')}`).pipe(
      map(res => res.entity)
    )
  }
  submitOTP(request: any) {
    return this.http.post<any>(`${BASE_URL}public/validate-otp`, request).pipe(
      map(res => res.entity)
    )
  }

  resetPassword(password: string) {
    const email = localStorage.getItem('email');
    return this.http.post<any>(`${BASE_URL}public/reset-password?email=${email}&password=${password}`, {})

  }
}
