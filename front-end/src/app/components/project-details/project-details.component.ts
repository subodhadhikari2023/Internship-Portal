import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.component.html',
  styleUrls: ['./project-details.component.css']
})
export class ProjectDetailsComponent implements OnInit {

  projectDetails: any = null;
  projectId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private internshipService: InternshipService
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



}
