import { Component, OnInit } from '@angular/core';
import { InternshipService } from "../../services/internship.service";
import { ApplicationService } from "../../services/application.service";

@Component({
  selector: 'app-view-application',
  templateUrl: './view-application.component.html',
  styleUrls: ['./view-application.component.css']
})
export class ViewApplicationComponent implements OnInit {
  applications: any[] = [];  
  filteredApplications: any[] = []; 
  selectedStatus: string = 'ALL'; 

  constructor(private applicationService: ApplicationService) { }

  ngOnInit(): void {
    this.loadApplications();
  }

  loadApplications(): void {
    this.applicationService.getAllApplications().subscribe({
      next: (response) => {
        this.applications = response;
        this.filteredApplications=response;
        console.log(this.applications);
      },
      error: (error: any) => {
        console.log(error);
      }
    });
  }

  filterApplications(event: any) {
    const status = event.target.value.toUpperCase();
    this.selectedStatus = status; // 🆕 Set current filter status

    if (status === 'ALL') {
      this.filteredApplications = this.applications;
    } else {
      this.filteredApplications = this.applications.filter(
        app => app.status.toUpperCase() === status
      );
    }
  }
}
