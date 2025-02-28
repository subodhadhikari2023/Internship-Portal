import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IntegrationService } from 'src/app/services/integration.service';

@Component({
  selector: 'app-students-home',
  templateUrl: './students-home.component.html',
  styleUrls: ['./students-home.component.css']
})
export class StudentsHomeComponent implements OnInit {
  message: string = '';
  userForm!: FormGroup;


  constructor(private fb: FormBuilder, private integrationService: IntegrationService) { }
  ngOnInit(): void {
    this.userForm = this.fb.group({});

  }



  accessMessage() {

    console.log('Button clicked!');
    // console.log(localStorage.getItem('token'));
    this.integrationService.accessMessage().subscribe({
      next: (response) => {
        // console.log(localStorage.getItem('token'));

        this.message = response.token;
      },
      error: (error) => {
        console.error('Error occurred:', error);
      }
    });
  }


}
