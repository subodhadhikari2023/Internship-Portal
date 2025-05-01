import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {
  private apiUrl = 'http://localhost:8080/internship-portal/api/v1/public/validate-certificate';

  constructor(private http: HttpClient) {}

  validateCertificate(certificateId: string): Observable<any> {
    const params = new HttpParams().set('certificateId', certificateId);
    return this.http.get<any>(this.apiUrl, { params }).pipe(map(res=>res.entity));
  }
}
