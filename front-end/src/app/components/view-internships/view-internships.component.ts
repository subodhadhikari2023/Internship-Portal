import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-view-internships',
  templateUrl: './view-internships.component.html',
  styleUrls: ['./view-internships.component.css']
})
export class ViewInternshipsComponent implements OnInit {
  internships: any[] = [];
filteredInternships:any[] = [];
  constructor(private internshipService: InternshipService) { }

  ngOnInit(): void {
    this.getInternships();
  }
  getInternships() {
    this.internshipService.getInternships().subscribe({
      next: (response) => {
        this.internships = response;
        this.filteredInternships=response;
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  filterInternships(event: any) {
    const status = event.target.value.toUpperCase(); 
    if (status === 'ALL') {
      this.filteredInternships = this.internships;
    } else {
      this.filteredInternships = this.internships.filter(internship => internship.status.toUpperCase() === status);
    }
  }
  
}
