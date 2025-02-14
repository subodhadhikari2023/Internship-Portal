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
    "assets/images/main/dham.jpg",
    "assets/images/main/house.jpg",
    "assets/images/main/mountaintop.jpg",
    "assets/images/main/peaks.jpg",
    "assets/images/main/river.jpg",
    "assets/images/main/waterfall.jpg",
    "assets/images/main/whitepeaks.jpg",
 

  ];
  heroTexts = [
    'Welcome to Sikkim\'s Internship Portal!',
    'Explore New Opportunities in the Himalayas!',
    'Join Us for an Unforgettable Experience!',
    'Discover the Tranquility of Buddha\'s Teachings!',
    'Experience the Rich Culture of Dham!',
    'Enjoy the Serenity of Traditional Sikkimese Houses!',
    'Reach New Heights at Our Mountain Tops!',
    'Witness the Majestic Peaks of Sikkim!',
    'Immerse Yourself in the Beauty of Rivers and Waterfalls!'
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
}
