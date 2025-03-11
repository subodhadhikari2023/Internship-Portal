import { Component, OnInit } from '@angular/core';
import { ApplicationService } from 'src/app/services/application.service';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-view-applications',
  templateUrl: './view-applications.component.html',
  styleUrls: ['./view-applications.component.css']
})
export class ViewApplicationsComponent implements OnInit {
viewDetails(_t23: any) {
throw new Error('Method not implemented.');
}
rejectApplication(_t23: any) {
throw new Error('Method not implemented.');
}
approveApplication(_t23: any) {
throw new Error('Method not implemented.');
}
  aplicationList: any[] = [];
  constructor(private applicationService: ApplicationService) { }

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

}
