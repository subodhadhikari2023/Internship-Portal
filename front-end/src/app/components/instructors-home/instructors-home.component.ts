import { Component, OnInit } from '@angular/core';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-instructors-home',
  templateUrl: './instructors-home.component.html',
  styleUrls: ['./instructors-home.component.css']
})
export class InstructorsHomeComponent implements OnInit {
  internships: number = 0

  constructor(private internshipService: InternshipService) { }

  ngOnInit(): void {
    this.internshipService.totalInternships().subscribe({
      next: (res) => {
        this.internships = res;
      },error:(err)=>{
        console.error(err);
        
      }
    })
  }

}
