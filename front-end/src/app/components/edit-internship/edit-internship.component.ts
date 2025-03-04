import { Component, OnInit } from '@angular/core';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-edit-internship',
  templateUrl: './edit-internship.component.html',
  styleUrls: ['./edit-internship.component.css']
})
export class EditInternshipComponent implements OnInit {
  internships: any[] = [];

  constructor(private internshipService: InternshipService) { }

  ngOnInit(): void {
    this.getInternships();
  }
  getInternships() {
    this.internshipService.getInternships().subscribe({
      next: (response) => {
        this.internships = response;
      
      },
      error: (err) => {
        console.log(err);
      }
    })
  }
}
