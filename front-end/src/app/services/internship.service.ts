import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { InternshipCreationRequest } from '../modals/internship-creation-request';
import { map, Observable } from 'rxjs';
const BASE_URL = "http://127.0.0.1:8080/internship-portal/api/v1/";

@Injectable({
  providedIn: 'root'
})
export class InternshipService {


  constructor(private http: HttpClient) { }

  createInternship(request: InternshipCreationRequest): Observable<any> {
    return this.http.post<InternshipCreationRequest>(`${BASE_URL}instructors/internship`, request);

  }


  getInternships(): Observable<any[]> {
    return this.http.get<any>(`${BASE_URL}instructors/internship`).pipe(
      map(response => response.entity || [])
    );
  }

  updateInternship(internship: any) {
    return this.http.put<any>(`${BASE_URL}instructors/internship`, internship);
  }
  getInternshipsForStudents() {
    return this.http.get<any>(`${BASE_URL}students/view-internships`).pipe(
      map(response => response.entity || [])
    );
  }

  applyForInternship(request: any) {
    return this.http.post<any>(`${BASE_URL}students/apply`, request);
  }
  hasStudentApplied(internshipId: number) {
    return this.http.get<{ entity: boolean }>(`${BASE_URL}students/has-applied/${internshipId}`);

  }
  getallDepartments() {
    return this.http.get<any>(`${BASE_URL}students/departments`).pipe(
      map(response => response.entity)
    );
  }

  getSelectedInternshipsForStudents() {
    return this.http.get<any>(`${BASE_URL}students/selected-internships`).pipe(
      map(response => response.entity)
    )
  }

  getStudentsEnrolledInInternshipsCreated() {
    return this.http.get<any>(`${BASE_URL}instructors/internship-students`).pipe(
      map(response => response.entity)
    )
  }


  createProject(request: any) {
    return this.http.post<any>(`${BASE_URL}instructors/create-project`, request).pipe(
      map(res => res.entity)
    )
  }
}
