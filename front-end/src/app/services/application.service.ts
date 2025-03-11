import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { map, Observable } from 'rxjs';
const BASE_URL = "http://127.0.0.1:8080/internship-portal/api/v1/";

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  constructor(private http: HttpClient) { }

  getAllApplications(): Observable<any[]> {
    return this.http.get<any>(`${BASE_URL}students/view-submitted-applications`).pipe(
      map(response => response.entity)
    );
  }
  getApplicationsForInstructor() {
    return this.http.get<any>(`${BASE_URL}instructors/applications`).pipe(
      map(response => response.entity)
    )
  }
}
