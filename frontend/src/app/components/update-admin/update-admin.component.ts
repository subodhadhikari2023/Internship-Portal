import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-update-admin',
  templateUrl: './update-admin.component.html',
  styleUrls: ['./update-admin.component.css']
})
export class UpdateAdminComponent implements OnInit {

  adminData: any = {};
  isEditing: boolean = false;
  selectedProfilePicture: File | null = null;
  profilePicturePreview: string | ArrayBuffer | null = null;
  originalData: any = {}; // Store original data to revert on cancel
  profilePictureSafeUrl: any;
  previewImage: string | ArrayBuffer | null = null;




  showPasswordInputs: boolean = false;
  passwordVerified: boolean = false;

  oldPassword: string = '';
  newPassword: string = '';
  passwordError: string = '';
  newPasswordError: string = '';


  togglePasswordUpdate() {
    this.showPasswordInputs = true;
  }
  
  verifyOldPassword() {
    if (!this.oldPassword) {
      this.passwordError = "Old password is required.";
      return;
    }
  
    this.userService.validatePassword(this.oldPassword).subscribe({
      next: (res) => {
        if (res === true) {
          this.passwordVerified = true;
          this.passwordError = '';
        } else {
          this.passwordError = "Old password is incorrect.";
        }
      },
      error: (err) => {
        console.error(err);
        this.passwordError = "Something went wrong. Try again.";
      }
    });
  }
  
  updatePassword() {
    if (!this.newPassword || this.newPassword.length < 6) {
      this.newPasswordError = "Password must be at least 6 characters.";
      return;
    }
  
    this.userService.updatePassword(this.newPassword).subscribe({
      next: () => {
        this.resetPasswordFields();
      },
      error: (err) => {
        console.error(err);
        this.newPasswordError = "Failed to update password.";
      }
    });
  }
  
  resetPasswordFields() {
    this.oldPassword = '';
    this.newPassword = '';
    this.passwordError = '';
    this.newPasswordError = '';
    this.passwordVerified = false;
    this.showPasswordInputs = false;
  }
  


  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.fetchInstructorDetails();
  }

  fetchInstructorDetails() {
    this.userService.fetchAdminDetails().subscribe({
      next: (res) => {
        // console.log(res);
        this.adminData = res;
        this.originalData = res;
        this.loadProfilePicture(this.adminData.profilePictureFilePath)
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
    this.adminData = { ...this.originalData }; // Revert changes
  }

  updateProfile() {
    console.log(this.adminData);
    this.userService.updateAdmin(this.adminData).subscribe({
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

    this.userService.uploadAdminProfilePicture(this.selectedProfilePicture).subscribe({
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

}
