import { Component, OnInit } from '@angular/core';

import { ApplicationService } from 'src/app/services/application.service';
import { InternshipService } from 'src/app/services/internship.service';
import { ViewApplicationModalComponent } from '../view-application-modal/view-application-modal.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-view-applications',
  templateUrl: './view-applications.component.html',
  styleUrls: ['./view-applications.component.css']
})
export class ViewApplicationsComponent implements OnInit {

  rejectApplication(_t23: any) {
    throw new Error('Method not implemented.');
  }
  approveApplication(_t23: any) {
    throw new Error('Method not implemented.');
  }
  aplicationList: any[] = [];
  constructor(private applicationService: ApplicationService, private dialog: MatDialog) { }

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
  viewDetails(application: any) {
    this.dialog.open(ViewApplicationModalComponent, {
      width: '400px',
      data: application
    });
  }

}
