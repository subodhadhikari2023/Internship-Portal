import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { InternshipService } from 'src/app/services/internship.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.css']
})
export class ProjectDetailsComponent implements OnInit {
  projectDetails: any = null;
  projectId: number | null = null;
  sanitizedPdfUrl: SafeResourceUrl | null = null;
  projectStatus: any | null;

  constructor(
    private route: ActivatedRoute,
    private internshipService: InternshipService,
    private sanitizer: DomSanitizer
  ) { }

  ngOnInit(): void {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.projectId) {
      this.fetchProjectDetails();
    }
  }

  fetchProjectDetails(): void {
    this.internshipService.getStudentsEnrolledInInternshipsCreated().subscribe({
      next: (internships) => {
        for (let internship of internships) {
          const foundProject = internship.projects.find(
            (project: any) => project.projectId === this.projectId
          );

          if (foundProject) {
            this.projectDetails = foundProject;

            // ✅ Sanitize PDF URL
            if (this.projectDetails.projectDescriptionFile) {
              this.sanitizedPdfUrl = this.sanitizeUrl(this.projectDetails.projectDescriptionFile);
            }
            break;
          }
        }

        console.log("Final projectDetails:", this.projectDetails);
      },
      error: (err) => {
        console.error("Error fetching project details:", err);
      }
    });
  }

  sanitizeUrl(url: string): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }
  downloadFile(filePath: string) {
    this.internshipService.downloadFile(filePath).subscribe({
      next: (blob) => {
        if (!blob) {
          console.error("Empty file response!");
          return;
        }

        const fileName = filePath.split('/').pop() || "downloaded_file";
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


  markProjectAsComplete(): void {
    if (!this.projectDetails || !this.projectId) return;

    const updateData = {
      projectId: this.projectId,
      status: "COMPLETED",
    };

    this.internshipService.markProjectAsComplete(updateData).subscribe({
      next: (response) => {
        console.log("Project marked as complete:", response);
        this.fetchProjectDetails();
      },
      error: (err) => {
        console.error("Error marking project as complete:", err);
      },
    });
  }


}
