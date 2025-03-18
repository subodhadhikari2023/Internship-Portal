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
    if (!filePath) {
      console.error("No file path provided!");
      return;
    }
  
    const apiUrl = `http://localhost:8080/internship-portal/api/v1/instructors/download?filePath=${encodeURIComponent(filePath)}`;
  
    fetch(apiUrl, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`, // Include JWT if needed
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to download file");
        }
        return response.blob();
      })
      .then((blob) => {
        const blobUrl = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = blobUrl;
        link.setAttribute("download", "Project_Description.pdf"); // Set filename
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(blobUrl); // Clean up
      })
      .catch((error) => console.error("Download error:", error));
  }
  
  
}
