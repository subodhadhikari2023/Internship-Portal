import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-view-internship-student',
  templateUrl: './view-internship-student.component.html',
  styleUrls: ['./view-internship-student.component.css']
})
export class ViewInternshipStudentComponent implements OnInit {

  certificateClaimStatus: boolean = false;
  internships: any[] = []
  certificateFilePath: any;
  constructor(private internshipService: InternshipService,private cdRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.viewSelectedInternships();
  }

 
  viewSelectedInternships() {
    this.internshipService.getSelectedInternshipsForStudents().subscribe({
      next: (response) => {
        this.internships = response.map((internship: any) => {
          // Check if the certificateFilePath exists and is not an empty string
          if (!internship.certificateFilePath || internship.certificateFilePath.trim() === '') {
            // Only claim the certificate if all projects are completed
            const allProjectsCompleted = internship.projects?.length > 0 &&
              internship.projects.every((project: any) => project.projectStatus === 'COMPLETED');

            if (allProjectsCompleted) {
              this.claimCertificate(internship.internshipStudentId);
            }
          }
          return internship;
        });
      },
      error: (err) => {
        console.log(err);
      }
    });
  }


  getProjectNames(internship: any): string {
    if (internship.projects && Array.isArray(internship.projects)) {
      return internship.projects.map((project: { projectName: any; }) => project.projectName).join(', ');
    }
    return 'No projects assigned.🫠';
  }

  claimCertificate(internshipStudentId: any) {
    return this.internshipService.claimCertificate(internshipStudentId).subscribe({
      next: (res) => {
        console.log(res);
        this.certificateFilePath = res.certificateFilePath;
        const internship = this.internships.find(i => i.internshipStudentId === internshipStudentId);
        if (internship) {
          internship.certificateFilePath = res.certificateFilePath;
        }
  
        // Force UI to update
        this.cdRef.detectChanges();
      }, error: (err) => {
        if (err.status == 409) {
          console.log("Already claimed the certificate");

        }

      }
    });
  }


  downloadCertificate(filePath: string) {
    if (!filePath) {
      console.error("Certificate file path is empty!");
      return;
    }
    console.log("Attempting to download certificate:", filePath);

    this.internshipService.downloadFileForStudents(filePath).subscribe({
      next: (blob) => {
        console.log("File received:", blob);
        if (!blob || blob.size === 0) {
          console.error("Received an empty file!");
          return;
        }

        const fileName = filePath.split('/').pop() || "Certificate.pdf";
        const blobUrl = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = blobUrl;
        link.setAttribute("download", fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(blobUrl);
      },
      error: (error) => {
        console.error("Download error:", error);
      }
    });
  }

}
