import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  uploadProjectFile(projectId: number, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<any>(
      `${BASE_URL}students/upload-project?projectId=${projectId}`,
      formData
    );
  }

  markProjectAsComplete(updateData: { projectId: number; status: string; }) {
    return this.http.post<any>(`${BASE_URL}instructors/change-project-status/${updateData.projectId}`, updateData.status);
  }

  downloadFile(filePath: string): Observable<Blob> {
    if (!filePath) {
      console.error("No file path provided!");
      throw new Error("File path is required");
    }

    const apiUrl = `${BASE_URL}public/download?filePath=${encodeURIComponent(filePath)}`;
    const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem("token")}`
    });

    return this.http.get(apiUrl, { headers, responseType: 'blob' });
  }
  downloadFileForStudents(filePath: string): Observable<Blob> {
    if (!filePath) {
      console.error("No file path provided!");
      throw new Error("File path is required");
    }

    const apiUrl = `${BASE_URL}public/download?filePath=${encodeURIComponent(filePath)}`;
    const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem("token")}`
    });

    return this.http.get(apiUrl, { headers, responseType: 'blob' });
  }

  claimCertificate(internshipStudentId: any) {
    return this.http.post<any>(`${BASE_URL}students/generate-certificate?internshipStudentId=${internshipStudentId}`, {}).pipe(
      map(res => res.entity)
    )

  }

  totalInternships(){
    return this.http.get<any>(`${BASE_URL}instructors/number-of-internships-created`).pipe(
      map(res=>res.entity)
    )
  }
  totalApplications(){
    return this.http.get<any>(`${BASE_URL}instructors/total-applications-of-internships-created`).pipe(
      map(res=>res.entity)
    )
  }

}
