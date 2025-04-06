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
