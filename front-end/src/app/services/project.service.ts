import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
const BASE_URL = "http://127.0.0.1:8080/internship-portal/api/v1/";
@Injectable({
  providedIn: 'root'
})
export class ProjectService {
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

  constructor(private http: HttpClient) { }

  getAllProjects() {
    return this.http.get<any>(`${BASE_URL}public/get-all-projects`).pipe(
      map(res => res.entity)
    )
  }
}
