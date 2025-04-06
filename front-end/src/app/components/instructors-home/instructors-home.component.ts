import { Component, OnInit } from '@angular/core';
import { AnonymousSubject } from 'rxjs/internal/Subject';
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
  recentInternships: any[] = [];
  recentApplications:any[] = [];
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

    this.internshipService.getFiveRecentInternships().subscribe({
      next: (res) => {
        this.recentInternships = res;

        
      }, error: (err) => {
        console.error(err);

      }
    })

    this.applicationService.getRecentApplications().subscribe({
      next:(res)=>{
        this.recentApplications=res;
        
      },error:(err)=>{
        console.error(err);
        
      }
    })
    

  }
  internshipDuration(start: string, end: string): string {
    const startDate = new Date(start);
    const endDate = new Date(end);
    const months = (endDate.getFullYear() - startDate.getFullYear()) * 12 + 
                   (endDate.getMonth() - startDate.getMonth());
    return `${months} month${months > 1 ? 's' : ''}`;
  }
  
}
