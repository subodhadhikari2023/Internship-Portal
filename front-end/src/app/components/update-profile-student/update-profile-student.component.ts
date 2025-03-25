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
  profilePictureSafeUrl: any; 

  constructor(private userService: UserService) { }

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
   console.log(this.studentData);
   

    
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
    this.userService.loadProfilePicture(filePath).subscribe((safeUrl) => {
      this.profilePictureSafeUrl = safeUrl;
    });
  }


  selectedFile: File | null = null;
  previewImage: string | ArrayBuffer | null = null;

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];

      // Preview the selected image
      const reader = new FileReader();
      reader.onload = () => {
        this.previewImage = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }
  uploadProfilePicture(): void {
    if (!this.selectedFile) {
      console.warn("No file selected!");
      return;
    }

    this.userService.uploadProfilePicture(this.selectedFile).subscribe({
      next: (response) => {
        console.log("Upload successful:", response);
        this.isEditing = false; // Set to read-only
        this.fetchStudentDetails(); // Fetch updated details and reload profile picture
      },
      error: (error) => {
        console.error("Error uploading profile picture:", error);
      }
    });
  }
  downloadResume() {
    this.userService.downloadResume(this.studentData.resumeFilePath).subscribe({
      next: (blob) => {
        const objectURL = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = objectURL;
        a.download = "resume.pdf";
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(objectURL);
      },
      error: (error) => console.error("File download error:", error)
    });
  }

  selectedResume: File | null = null;
  onResumeSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedResume = input.files[0];
    }
  }

  uploadResume() {
    if (!this.selectedResume) {
      console.log("No file selected.");
      return;
    }

    this.userService.uploadResume(this.selectedResume).subscribe({
      next: (res) => {
        console.log(res);
        this.fetchStudentDetails()

      }, error: (err) => {
        console.log(err);

      }
    });

  }
}