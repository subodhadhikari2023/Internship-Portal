import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { map, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

const BASE_URL = `${environment.apiBaseUrl}/`;

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

  setApplicationStatus(app: any, appStatus: any) {
    return this.http.post<any>(`${BASE_URL}instructors/review-applications?applicationId=${app.applicationId}`, appStatus);
  }

  getNumberOfApplications() {
    return this.http.get<any>(`${BASE_URL}instructors/pending-applications`).pipe(
      map(res=>res.entity)
    )
  }
  getRecentApplications(){
    return this.http.get<any>(`${BASE_URL}instructors/recent-applications`).pipe(
      map(res=>res.entity)
    )
  }
}
