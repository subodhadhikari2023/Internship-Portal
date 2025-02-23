import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  
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
    this.router.navigateByUrl('/home');
  }
}