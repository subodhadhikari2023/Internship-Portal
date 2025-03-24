import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { catchError, map, Observable, throwError } from 'rxjs';
const BASE_URL = "http://127.0.0.1:8080/internship-portal/api/v1/";
@Injectable({
  providedIn: 'root'
})
export class UserService {


  constructor(private http: HttpClient,private sanitizer:DomSanitizer ) { }

 
  

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
      .get(`${BASE_URL}students/download`, {
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
  
}
