import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-update-profile-student',
  templateUrl: './update-profile-student.component.html',
  styleUrls: ['./update-profile-student.component.css']
})
export class UpdateProfileStudentComponent implements OnInit {

  studentData: any = {};
  isEditing: boolean = false;

  constructor(private userService: UserService, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.fetchStudentDetails();
  }

  fetchStudentDetails() {
    this.userService.fetchStudentDetails().subscribe({
      next: (res) => {
        console.log(res);
        this.studentData = res;
        this.loadProfilePicture(this.studentData.profilePictureFilePath)
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

  loadProfilePicture(filePath: string) {
    if (!filePath) {
      console.error("No file path provided!");
      return;
    }

    const apiUrl = `http://localhost:8080/internship-portal/api/v1/students/download?filePath=${encodeURIComponent(filePath)}`;

    fetch(apiUrl, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch image");
        }
        return response.blob();
      })
      .then((blob) => {
        const objectURL = URL.createObjectURL(blob);
        this.studentData.profilePictureFilePath = this.sanitizer.bypassSecurityTrustUrl(objectURL);
      })
      .catch((error) => console.error("Image fetch error:", error));
  }

}
