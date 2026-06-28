import { Component, OnInit } from '@angular/core';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-view-enrolled-students',
  templateUrl: './view-enrolled-students.component.html',
  styleUrls: ['./view-enrolled-students.component.css']
})
export class ViewEnrolledStudentsComponent implements OnInit {
  students: any[] = [];
  constructor(private internshipService: InternshipService) { }

  ngOnInit(): void {
    this.viewStudentsEnrolledInInternshipsCreated();
  }

  viewStudentsEnrolledInInternshipsCreated() {
    this.internshipService.getStudentsEnrolledInInternshipsCreated().subscribe({
      next: (response) => {
        this.students=response;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }
  isLastProject(project: string, projects: string[]): boolean {
    return projects.indexOf(project) === projects.length - 1;
  }

}
