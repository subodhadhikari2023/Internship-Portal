import { Component, OnInit } from '@angular/core';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-popup',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.css']
})
export class PopupComponent implements OnInit {

  constructor(public notifier: NotificationService) { }

  isPopupOpen$ = this.notifier.isPopupOpen$;
  message$ = this.notifier.message$;
  color$ = this.notifier.color$;
  fontColor$ = this.notifier.fontColor$;
 


  ngOnInit(): void {
  }

}
