import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-view-internship-student',
  templateUrl: './view-internship-student.component.html',
  styleUrls: ['./view-internship-student.component.css']
})
export class ViewInternshipStudentComponent implements OnInit {
  internships:any []  = []
  constructor(private internshipService: InternshipService) { }

  ngOnInit(): void {
    this.viewSelectedInternships();
  }

  viewSelectedInternships() {
    this.internshipService.getSelectedInternshipsForStudents().subscribe({
      next: (response) => {
       this.internships=response;

      }, error: (err) => {
        console.log(err);

      }
    })
  }

}
