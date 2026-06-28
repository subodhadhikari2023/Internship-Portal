import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApplicationService } from 'src/app/services/application.service';

@Component({
  selector: 'app-view-applications',
  templateUrl: './view-applications.component.html',
  styleUrls: ['./view-applications.component.css']
})
export class ViewApplicationsComponent implements OnInit {

  aplicationList: any[] = [];
  constructor(private applicationService: ApplicationService, private http: Router) { }

  ngOnInit(): void {
    this.getAllApplications();
  }

  getAllApplications() {
    this.applicationService.getApplicationsForInstructor().subscribe({
      next: (res) => {
        this.aplicationList = res;
      }, error: (err) => {
        console.log(err);
      }
    })
  }
  viewDetails(_t23: any) {
    this.http.navigate(['/instructor/student-details/:id']);
  }

  updateApplicationStatus(request: any, appStatus: any) {

    this.applicationService.setApplicationStatus(request, appStatus).subscribe({
      next: () => {
        this.getAllApplications();
      }
    });


  }
}
