import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  menuOpen = false;
  dropdowns: { [key: string]: boolean } = { internships: false, applications: false, profile: false };
  instructorData: any = {};

  constructor(private router: Router,private userService:UserService) { }
  ngOnInit(): void {
    this.userService.fetchInstructorDetails().subscribe({
      next: (res) => {
        this.instructorData = res;
        if (res.profilePictureFilePath) {
          this.userService.loadProfilePicture(res.profilePictureFilePath).subscribe({
            next: (safeUrl) => {
              this.instructorData.profilePictureFilePath = safeUrl; // Correctly assigning SafeUrl
            },
            error: (error) => {
              console.error("Error loading profile picture:", error);
            }
          });
        }
      },
      error: (err) => {
        console.error("Error fetching student details:", err);
      }
    });  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  toggleDropdown(menu: string) {
    this.dropdowns[menu] = !this.dropdowns[menu];
  }

  logOut() {
    localStorage.removeItem('token');
    this.router.navigateByUrl('/');
  }
}