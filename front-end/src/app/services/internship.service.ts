import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { InternshipCreationRequest } from '../modals/internship-creation-request';
import { Observable } from 'rxjs';
const BASE_URL = "http://127.0.0.1:8080/internship-portal/api/v1/";

@Injectable({
  providedIn: 'root'
})
export class InternshipService {

  constructor(private http: HttpClient) { }

  createInternship(request: InternshipCreationRequest): Observable<any> {
    return this.http.post<InternshipCreationRequest>(`${BASE_URL}instructors/internship`, request);

  }
}
