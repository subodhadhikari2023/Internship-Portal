import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { catchError, map, Observable, throwError } from 'rxjs';
const BASE_URL = "http://127.0.0.1:8080/internship-portal/api/v1/";
@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient, private sanitizer: DomSanitizer) { }

  
  updateInstructorProfile(instructorData: any) {
    return this.http.put<any>(`${BASE_URL}instructors/update-profile`, instructorData).pipe(
      map(res => res.entity)
    )
  }
  fetchInstructorDetails() {
    return this.http.get<any>(`${BASE_URL}instructors/get-profile-details`).pipe(
      map(res => res.entity)
    )
  }


  fetchStudentDetails() {
    return this.http.get<any>(`${BASE_URL}students/get-profile-details`).pipe(
      map(res => res.entity)
    );
  }


  updateStudentProfile(studentData: any) {
    return this.http.put<any>(`${BASE_URL}students/update-profile`, studentData);
  }


  loadProfilePicture(filePath: string): Observable<SafeUrl> {
    if (!filePath) {
      return throwError(() => new Error("No file path provided!"));
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    });

    return this.http
      .get(`${BASE_URL}public/download`, {
        headers,
        params: { filePath },
        responseType: "blob",
      })
      .pipe(
        map((blob) => {
          const objectURL = URL.createObjectURL(blob);
          return this.sanitizer.bypassSecurityTrustUrl(objectURL);
        }),
        catchError((error) => {
          console.error("Error fetching profile picture:", error);
          return throwError(() => new Error("Failed to fetch profile picture"));
        })
      );
  }



  uploadProfilePicture(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file); 

    return this.http.post(`${BASE_URL}students/update-profile-picture`, formData);
  }
  uploadInstructorProfilePicture(selectedProfilePicture: File) {
    const formData = new FormData();
    formData.append("file", selectedProfilePicture);
    return this.http.post(`${BASE_URL}instructors/update-profile-picture`, formData)
    
  }

  downloadResume(filePath: string): Observable<Blob> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    });

    return this.http.get(`${BASE_URL}public/download`, {
      headers,
      params: { filePath },
      responseType: 'blob'
    });
  }
  fetchStudentDetailsForInstructor(studentId: any) {
    return this.http.get<any>(`${BASE_URL}instructors/get-student-details?studentId=${studentId}`).pipe(
      map(res => res.entity)
    );
  }

  uploadResume(selectedResume: File) {
    const formData = new FormData();
    formData.append("file", selectedResume);
    return this.http.post<any>(`${BASE_URL}students/upload-resume`, formData).pipe(
      map(res => res.entity)
    );
  }

}
