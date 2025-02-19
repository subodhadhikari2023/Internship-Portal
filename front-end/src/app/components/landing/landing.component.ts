import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {

  constructor() { }

  bannerImageUrls = [
    "assets/images/main/buddha.jpg",
    // "assets/images/main/dham.jpg",
    "assets/images/main/house.jpg",
    "assets/images/main/mountaintop.jpg",
    "assets/images/main/peaks.jpg",
    "assets/images/main/river.jpg",
    "assets/images/main/waterfall.jpg",
    "assets/images/main/whitepeaks.jpg",


  ];
  currentImageIndex = 0;


  ngOnInit() {
    setInterval(() => {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.bannerImageUrls.length;
    }, 3000);

  }

  isMenuOpen = false;

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  isNavbarOpen = false;

  toggleNavbar() {
    this.isNavbarOpen = !this.isNavbarOpen;
  }


}

