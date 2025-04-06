import { Component, OnInit } from '@angular/core';
import { ApplicationService } from 'src/app/services/application.service';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-instructors-home',
  templateUrl: './instructors-home.component.html',
  styleUrls: ['./instructors-home.component.css']
})
export class InstructorsHomeComponent implements OnInit {
  internships: number = 0;
  applications: number = 0;
  pendingApplications: number = 0;
  activeInternships: number = 0;
  constructor(private internshipService: InternshipService, private applicationService: ApplicationService) { }

  ngOnInit(): void {
    this.internshipService.totalInternships().subscribe({
      next: (res) => {
        this.internships = res;
      }, error: (err) => {
        console.error(err);

      }
    });

    this.internshipService.totalApplications().subscribe({
      next: (res) => {
        this.applications = res;
      }, error: (err) => {
        console.error(err);

      }
    })

    this.applicationService.getNumberOfApplications().subscribe({
      next: (res) => {
        this.pendingApplications = res;

      }, error: (err) => {
        console.error(err);

      }
    })


    this.internshipService.totalActiveInternships().subscribe({
      next: (res) => {
        this.activeInternships = res;
      }, error: (err) => {
        console.error(err);

      }
    })

  }

}
