import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs';

const BASE_URL = "http://127.0.0.1:8080/internship-portal/api/v1/administrator/";
@Injectable({
  providedIn: 'root'
})

export class AdminService {
  constructor(private http: HttpClient) { }

  getAllDepartments() {
    return this.http.get<any>(`${BASE_URL}department`).pipe(map(
      res => res.entity
    ))
  }

  createNewDepartment(departmentName: String) {
    const request = {
      entity: departmentName
    }
    return this.http.post<any>(`${BASE_URL}department`, request).pipe(
      map(res => res.entity)
    );
  }

  getAllUsers() {
    return this.http.get<any>(`${BASE_URL}users`).pipe(
      map(res => res.entity)
    );
  }
  getAllStudents() {
    return this.http.get<any>(`${BASE_URL}students`).pipe(
      map(res => res.entity)
    );
  }
  getAllInstructors() {
    return this.http.get<any>(`${BASE_URL}instructors`).pipe(
      map(res => res.entity)
    );
  }
  createInstructor(instructor: any) {
    return this.http.post<any>(`${BASE_URL}instructors`, instructor).pipe(map(res => res.entity));
  }

  updateInstructor(request: any) {
    return this.http.put<any>(`${BASE_URL}instructors`, request).pipe(
      map(res => res.entity)
    )
  }
}
