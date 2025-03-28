import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-view-project-details-student',
  templateUrl: './view-project-details-student.component.html',
  styleUrls: ['./view-project-details-student.component.css']
})
export class ViewProjectDetailsStudentComponent implements OnInit {
  selectedFile: File | null = null;
  isUploading: boolean = false;
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


  uploadProjectFile(): void {
    if (!this.selectedFile || !this.projectId) return;

    this.isUploading = true;
    this.internshipService.uploadProjectFile(this.projectId, this.selectedFile).subscribe({
      next: (response) => {
        this.fetchProjectDetails();

        this.selectedFile = null;
        this.isUploading = false;
      },
      error: (err) => {
        console.error("Upload failed:", err);
        this.isUploading = false;
      },
    });
  }

  onFileSelected(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files && inputElement.files.length > 0) {
      this.selectedFile = inputElement.files[0];
    }
  }


  fetchProjectDetails(): void {
    this.internshipService.getSelectedInternshipsForStudents().subscribe({
      next: (internships) => {
        let foundProject = null;


        for (let internship of internships) {
          foundProject = internship.projects?.find(
            (project: any) => project.projectId === this.projectId

          );

          if (foundProject) {
            console.log(foundProject);
            break;
          }
        }

        this.projectDetails = foundProject || { projectFile: null };

        // console.log("Project details loaded:", this.projectDetails);
      },
      error: (err) => {
        console.error("Error fetching project details:", err);
        this.projectDetails = { projectFile: null };
      },
    });
  }



  sanitizeUrl(url: string): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }
  downloadFile(filePath: string) {
    this.internshipService.downloadFileForStudents(filePath).subscribe({
      next: (blob) => {
        if (!blob) {
          console.error("Empty file response!");
          return;
        }
  
        const fileName = filePath.split('/').pop() || "Project_Description.pdf"; 
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
