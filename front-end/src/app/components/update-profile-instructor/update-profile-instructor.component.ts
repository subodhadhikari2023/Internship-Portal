import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-update-profile-instructor',
  templateUrl: './update-profile-instructor.component.html',
  styleUrls: ['./update-profile-instructor.component.css']
})
export class UpdateProfileInstructorComponent implements OnInit {

  instructorData: any = {};
  isEditing: boolean = false;
  selectedProfilePicture: File | null = null;
  profilePicturePreview: string | ArrayBuffer | null = null;
  originalData: any = {}; // Store original data to revert on cancel
  profilePictureSafeUrl: any;
  previewImage: string | ArrayBuffer | null = null;

  // Password update logic
  showPasswordInputs: boolean = false;
  passwordVerified: boolean = false;
  oldPassword: string = '';
  newPassword: string = '';
  passwordError: string = '';
  newPasswordError: string = '';


  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.fetchInstructorDetails();
  }

  fetchInstructorDetails() {
    this.userService.fetchInstructorDetails().subscribe({
      next: (res) => {
        // console.log(res);
        this.instructorData = res;
        this.originalData = res;
        this.loadProfilePicture(this.instructorData.profilePictureFilePath)
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

  enableEditing() {
    this.isEditing = true;
  }

  cancelEditing() {
    this.isEditing = false;
    this.instructorData = { ...this.originalData }; // Revert changes
  }

  updateProfile() {
    console.log(this.instructorData);
    this.userService.updateInstructorProfile(this.instructorData).subscribe({
      next: (res) => {
        this.isEditing = false;
        this.fetchInstructorDetails();
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  onProfilePictureSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedProfilePicture = input.files[0];

      // Preview Image
      const reader = new FileReader();
      reader.onload = () => {
        this.profilePicturePreview = reader.result;
      };
      reader.readAsDataURL(this.selectedProfilePicture);
    }
  }

  uploadProfilePicture(): void {
    console.log(this.selectedProfilePicture);

    if (!this.selectedProfilePicture) {
      console.warn("No file selected!");
      return;
    }

    this.userService.uploadInstructorProfilePicture(this.selectedProfilePicture).subscribe({
      next: (response) => {
        console.log("Upload successful:", response);
        this.isEditing = false;
        this.selectedProfilePicture = null;
        this.fetchInstructorDetails();
      },
      error: (error) => {
        console.error("Error uploading profile picture:", error);
      }
    });
  }
  togglePasswordUpdate(): void {
    this.showPasswordInputs = true;
    this.passwordVerified = false;
    this.oldPassword = '';
    this.newPassword = '';
    this.passwordError = '';
    this.newPasswordError = '';
  }

  verifyOldPassword(): void {
    if (!this.oldPassword) {
      this.passwordError = "Old password is required!";
      return;
    }

    this.userService.validatePassword(this.oldPassword).subscribe({
      next: (res: any) => {
        if (res === true) {
          this.passwordVerified = true;
          this.passwordError = '';
        } else {
          this.passwordError = "Old password is incorrect!";
        }
      },
      error: (err) => {
        console.error(err);
        this.passwordError = "Error verifying password!";
      }
    });
  }

  updatePassword(): void {
    if (!this.newPassword || this.newPassword.length < 6) {
      this.newPasswordError = "Password must be at least 6 characters long!";
      return;
    }

    this.userService.updatePassword(this.newPassword).subscribe({
      next: () => {
        this.resetPasswordFields();
      },
      error: (err) => {
        console.error(err);
        this.newPasswordError = "Error updating password!";
      }
    });
  }

  resetPasswordFields(): void {
    this.showPasswordInputs = false;
    this.passwordVerified = false;
    this.oldPassword = '';
    this.newPassword = '';
    this.passwordError = '';
    this.newPasswordError = '';
  }

}
