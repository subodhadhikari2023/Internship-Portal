import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { InternshipService } from 'src/app/services/internship.service';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-students-home',
  templateUrl: './students-home.component.html',
  styleUrls: ['./students-home.component.css']
})
export class StudentsHomeComponent implements OnInit {

  message: string = '';
  userForm!: FormGroup;
  internships: any[] = [];
  filteredInternships: any[] = [];
  constructor(private internshipService: InternshipService,private notifier:NotificationService) { }

  ngOnInit(): void {
    this.getInternships();
  }
  getInternships() {
    this.internshipService.getInternshipsForStudents().subscribe({
      next: (response) => {
        this.internships = response;
        this.filteredInternships = response;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  filterInternships(event: any) {
    const department = event.target.value.toUpperCase();
    if (department === 'ALL') {
      this.filteredInternships = this.internships;
    } else {
      this.filteredInternships = this.internships.filter(internship => internship.department.toUpperCase() === department);
    }
  }

  applyInternship(internship: any) {
    const requestBody = { internship: { internshipId: internship.internshipId } };
    this.internshipService.applyForInternship(requestBody).subscribe({
      next: () => {
        this.notifier.openPopup('Application Submitted successfully','red','Roboto',5000);
      },
      error: (err) => {
        if(err.status==409){
          this.notifier.openPopup('Already applied for the internship!','red','Roboto',5000 );
        }
      }
    });
  }
  

}
