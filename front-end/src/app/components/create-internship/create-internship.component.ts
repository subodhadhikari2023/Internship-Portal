import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InternshipCreationRequest } from 'src/app/modals/internship-creation-request';
import { InternshipService } from 'src/app/services/internship.service';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-create-internship',
  templateUrl: './create-internship.component.html',
  styleUrls: ['./create-internship.component.css', './create-internship-media.component.css'],
  animations: []
})
export class CreateInternshipComponent {
  userForm: FormGroup;
  internshipRequest: InternshipCreationRequest = new InternshipCreationRequest();
  errorMessage: string = '';
  constructor(private fb: FormBuilder, private internship: InternshipService, public notifier: NotificationService) {
    this.userForm = this.fb.group({
      internshipName: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      educationalQualifications: ['', Validators.required],
      status: ['', Validators.required],
      requiredSkills: ['', Validators.required],
      workMode: ['', Validators.required],
      description: ['', Validators.required],


    })
  }


  createInternship() {

    this.internshipRequest = { ...this.userForm.value };

    if (typeof (this.userForm.value.requiredSkills) == 'string') {
      this.internshipRequest.requiredSkills = this.userForm.value.requiredSkills.split(",");
    }


    const startDate = new Date(this.internshipRequest.startDate);
    const endDate = new Date(this.internshipRequest.endDate);

    const duration = (endDate.getTime() - startDate.getTime()) / (1000 * 3600 * 24);
    if (duration < 90) {
      this.errorMessage = "Internship Duration should be atleast 90 days or more!";
      return;
    }
    this.internship.createInternship(this.internshipRequest).subscribe({
      next: (response: any) => {

        this.notifier.openPopup('Internship Created Successfully!', '#88d286', 'white', 5000);
        this.userForm.reset();
      }, error: (err) => {
        if (err.status == 401) {
          this.errorMessage = "Fill up the form fields correctly!";
        }
      }
    });




  }


}
