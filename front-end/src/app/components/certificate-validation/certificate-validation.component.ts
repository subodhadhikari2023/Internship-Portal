import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CertificateService } from 'src/app/services/certificate.service';

@Component({
  selector: 'app-certificate-validation',
  templateUrl: './certificate-validation.component.html',
  styleUrls: ['./certificate-validation.component.css']
})
export class CertificateValidationComponent implements OnInit {

  certificateId: string | null = null;
  result: any;
  error: string | null = null;
  constructor(private route: ActivatedRoute, private certificateService: CertificateService) {}

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      this.certificateId = params.get('certificateId');
      if (this.certificateId) {
        this.validateCertificate(this.certificateId);
      } else {
        this.error = 'No certificate ID provided in the URL!';
      }
    });
  }

  validateCertificate(certificateId: string) {
    this.certificateService.validateCertificate(certificateId)
      .subscribe({
        next: (data) => {
          this.result = data;
          this.error = null;
        },
        error: (err) => {
          if (err.status === 404) {
            this.error = 'Certificate not found!';
          } else {
            this.error = 'An error occurred.';
          }
          this.result = null;
        }
      });
  }

}
