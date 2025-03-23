import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-update-profile-student',
  templateUrl: './update-profile-student.component.html',
  styleUrls: ['./update-profile-student.component.css']
})
export class UpdateProfileStudentComponent implements OnInit {

  studentData: any = {};
  isEditing: boolean = false;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.fetchStudentDetails();
  }

  fetchStudentDetails() {
    this.userService.fetchStudentDetails().subscribe({
      next: (res) => {
        // console.log(res);        
        this.studentData = res;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  enableEditing() {
    this.isEditing = true;
  }

  cancelEditing() {
    this.isEditing = false;
    this.fetchStudentDetails(); // Reset changes if canceled
  }

  updateProfile() {
    if (typeof (this.studentData.skills) == 'string') {
      this.studentData.skills = this.studentData.skills.split(",");
    }
    this.userService.updateStudentProfile(this.studentData).subscribe({
      next: (res) => {
        this.isEditing = false;
        this.fetchStudentDetails();
       


      },
      error: (err) => {
        console.error(err);
      }
    });
  }
}
