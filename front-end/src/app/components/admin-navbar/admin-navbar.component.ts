import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-admin-navbar',
  templateUrl: './admin-navbar.component.html',
  styleUrls: ['./admin-navbar.component.css']
})
export class AdminNavbarComponent implements OnInit {

  dropdowns: { [key: string]: boolean } = { profile: false };
  adminData: any = {};

  constructor(private router: Router, private userService: UserService) {}

  ngOnInit(): void {
    this.userService.fetchAdminDetails().subscribe({
      next: (res) => {
        
        this.adminData = res;
        if (res.profilePictureFilePath) {
          this.userService.loadProfilePicture(res.profilePictureFilePath).subscribe({
            next: (safeUrl) => {
              this.adminData.profilePictureFilePath = safeUrl;
            },
            error: (error) => console.error("Error loading profile picture:", error)
          });
        }
      },
      error: (err) => console.error("Error fetching admin details:", err)
    });
  }

  toggleDropdown(menu: string) {
    this.dropdowns[menu] = !this.dropdowns[menu];
  }

  logOut() {
    localStorage.removeItem('token');
    this.router.navigateByUrl('/');
  }

}
