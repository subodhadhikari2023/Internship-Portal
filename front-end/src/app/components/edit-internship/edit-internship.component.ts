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

  updateInternship(internship: any) {
    internship.isEditing = false;

    // Ensure internshipId is included in the request payload
    const updatedInternship = {
      internshipId: internship.internshipId, // Ensure ID is present
      internshipName: internship.internshipName,
      startDate: internship.startDate,
      endDate: internship.endDate,
      description: internship.description,
      educationalQualifications: internship.educationalQualifications,
      workMode: internship.workMode,
      status: internship.status,
      // requiredSkills: internship.requiredSkills
      requiredSkills: Array.isArray(internship.requiredSkills)
        ? internship.requiredSkills
        : internship.requiredSkills.split(',').map((skill: string) => skill.trim())
    };
    // console.log(updatedInternship);

    this.internshipService.updateInternship(updatedInternship).subscribe({
      next: (response) => {
        console.log("Updated Internship:", response);
      },
      error: (err) => {
        console.error("Error updating internship:", err);
      }
    });
  }




  editInternship(internship: any) {
    internship.isEditing = true;
  }
}
