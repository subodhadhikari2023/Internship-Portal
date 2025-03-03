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
      map(response => response.entity || []) // Extract the entity array
    );
  }
}
