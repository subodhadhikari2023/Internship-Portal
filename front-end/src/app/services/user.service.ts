import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs';
const BASE_URL = "http://127.0.0.1:8080/internship-portal/api/v1/";
@Injectable({
  providedIn: 'root'
})
export class UserService {


  constructor(private http:HttpClient) { }



  fetchStudentDetails(){
    return this.http.get<any>(`${BASE_URL}students/get-profile-details`).pipe(
      map(res=>res.entity)
    );
  }


  updateStudentProfile(studentData: any) {
    return this.http.put<any>(`${BASE_URL}students/update-profile`,studentData);
  }
}
