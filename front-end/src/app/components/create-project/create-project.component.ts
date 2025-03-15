import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InternshipService } from 'src/app/services/internship.service';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {
  projectForm!: FormGroup;
  students: { email: string, internships: string[] }[] = [];
  selectedInternships: string[] = [];

  constructor(private fb: FormBuilder, private internshipService: InternshipService) { }

  ngOnInit(): void {
    this.getStudentsAndInternships();
    this.initializeForm();
  }

  getStudentsAndInternships() {
    this.internshipService.getStudentsEnrolledInInternshipsCreated().subscribe({
      next: (res) => {
        const studentInternshipMap = new Map<string, string[]>();

        res.forEach((item: any) => {
          if (!studentInternshipMap.has(item.userEmail)) {
            studentInternshipMap.set(item.userEmail, []);
          }
          studentInternshipMap.get(item.userEmail)?.push(item.internshipName);
        });

        this.students = Array.from(studentInternshipMap, ([email, internships]) => ({
          email,
          internships
        }));
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

  initializeForm() {
    this.projectForm = this.fb.group({
      studentEmail: ['', Validators.required],
      internship: ['', Validators.required],
      projectName: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  onStudentChange(event: Event) {
    const studentEmail = (event.target as HTMLSelectElement).value;
    const selectedStudent = this.students.find(student => student.email === studentEmail);
    this.selectedInternships = selectedStudent ? selectedStudent.internships : [];
    this.projectForm.controls['internship'].setValue(''); // Reset internship dropdown
  }

  submitProject() {
    if (this.projectForm.valid) {
      console.log("Project Data:", this.projectForm.value);
      // Call backend API to save the project
    } else {
      console.log("Form is invalid");
    }
  }


}
