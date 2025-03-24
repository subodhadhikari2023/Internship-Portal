import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-student-nav',
  templateUrl: './student-nav.component.html',
  styleUrls: ['./student-nav.component.css']
})
export class StudentNavComponent implements OnInit {
  studentData: any = {};

  ngOnInit(): void {
    this.userService.fetchStudentDetails().subscribe({
      next: (res) => {
        this.studentData = res;
        if (res.profilePictureFilePath) {
          this.userService.loadProfilePicture(res.profilePictureFilePath).subscribe({
            next: (safeUrl) => {
              this.studentData.profilePictureFilePath = safeUrl; // Correctly assigning SafeUrl
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
    });
  }
  menuOpen = false;
  dropdowns: { [key: string]: boolean } = { internships: false, applications: false, profile: false };

  constructor(private router: Router, private userService: UserService) { }

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
