import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-student-nav',
  templateUrl: './student-nav.component.html',
  styleUrls: ['./student-nav.component.css']
})
export class StudentNavComponent implements OnInit {


  ngOnInit(): void {
  }
  menuOpen = false;
  dropdowns: { [key: string]: boolean } = { internships: false, applications: false, profile: false };

  constructor(private router: Router) { }

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
