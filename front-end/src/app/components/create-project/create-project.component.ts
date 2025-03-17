import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InternshipService } from 'src/app/services/internship.service';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {
  projectForm!: FormGroup;
  submissionError = "";
  students: {
    email: string,
    internships: { name: string, startDate: string, endDate: string }[]
  }[] = [];
  selectedInternships: { name: string, startDate: string, endDate: string }[] = [];


  constructor(private fb: FormBuilder, private internshipService: InternshipService, private notifier: NotificationService) { }

  ngOnInit(): void {
    this.getStudentsAndInternships();
    this.initializeForm();
  }

  getStudentsAndInternships() {
    this.internshipService.getStudentsEnrolledInInternshipsCreated().subscribe({
      next: (res) => {
        const studentInternshipMap = new Map<string, { name: string, startDate: string, endDate: string }[]>();

        res.forEach((item: any) => {
          if (!studentInternshipMap.has(item.userEmail)) {
            studentInternshipMap.set(item.userEmail, []);
          }
          studentInternshipMap.get(item.userEmail)?.push({
            name: item.internshipName,
            startDate: item.startDate,
            endDate: item.endDate
          });
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
      projectName: ['', [Validators.required, Validators.minLength(3)]],
      submissionDate: ['', Validators.required],
      projectDescription: ['', Validators.required],
      descriptionFile: [null, Validators.required] 


    });
  }

  onStudentChange(event: Event) {
    const studentEmail = (event.target as HTMLSelectElement).value;
    const selectedStudent = this.students.find(student => student.email === studentEmail);
    this.selectedInternships = selectedStudent ? selectedStudent.internships : [];
    this.projectForm.controls['internship'].setValue('');
  }

  selectedInternshipStartDate: string = "";
  selectedInternshipEndDate: string = "";

  onInternshipChange(event: Event) {
    const internshipName = (event.target as HTMLSelectElement).value;
    const selectedInternship = this.selectedInternships.find(internship => internship.name === internshipName);

    if (selectedInternship) {
      this.selectedInternshipStartDate = selectedInternship.startDate;
      this.selectedInternshipEndDate = selectedInternship.endDate;
    } else {
      this.selectedInternshipStartDate = "";
      this.selectedInternshipEndDate = "";
    }

    this.validateSubmissionDate();
  }




  validateSubmissionDate() {
    const submissionDate = new Date(this.projectForm.controls['submissionDate'].value);
    const today = new Date();
    const startDate = new Date(this.selectedInternshipStartDate);
    const endDate = new Date(this.selectedInternshipEndDate);

    if (submissionDate < today) {
      this.submissionError = "Submission date cannot be in the past.";
      this.projectForm.controls['submissionDate'].setErrors({ invalidDate: true });
    } else if (submissionDate < startDate || submissionDate > endDate) {
      this.submissionError = ` Submission date must be between ${this.selectedInternshipStartDate} and ${this.selectedInternshipEndDate}.`;
      this.projectForm.controls['submissionDate'].setErrors({ invalidRange: true });
    } else {
      this.submissionError = "";
      this.projectForm.controls['submissionDate'].setErrors(null);
    }
  }



  submitProject() {
    if (this.projectForm.invalid || !this.selectedFile) {
      console.log("Form is invalid or file not selected");
      return;
    }
  
    const projectData = {
      projectName: this.projectForm.value.projectName,
      projectDescription: this.projectForm.value.projectDescription,
      studentEmail: this.projectForm.value.studentEmail,
      internshipName: this.projectForm.value.internship,
      submissionDate: this.projectForm.value.submissionDate
    };
  

    const formData = new FormData();
    formData.append("project", new Blob([JSON.stringify(projectData)], { type: "application/json" }));
    formData.append("file", this.selectedFile);
  
    this.internshipService.createProject(formData).subscribe({
      next: (res) => {
        this.projectForm.reset();
        this.selectedFile = null;
        this.notifier.openPopup('Project Created and Assigned Successfully!', '#88d286', 'white', 5000);
      },
      error: (err) => {
        console.log(err);
      }
    });
  }
  
  selectedFile: File | null = null;

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }
  

}
