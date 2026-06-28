import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private isPopupOpenSubject = new BehaviorSubject<boolean>(false);
  isPopupOpen$ = this.isPopupOpenSubject.asObservable();

  private messageSubject = new BehaviorSubject<string>('');
  message$ = this.messageSubject.asObservable();

  private colorSubject = new BehaviorSubject<string>('');
  color$ = this.colorSubject.asObservable();

  private fontColorSubject = new BehaviorSubject<string>('');
  fontColor$ = this.fontColorSubject.asObservable();

  private timeoutRef: any;  

  openPopup(message: string, color: string, font: string, duration: number) {
    this.closePopup();

    this.isPopupOpenSubject.next(true);
    this.messageSubject.next(message);
    this.colorSubject.next(color);
    this.fontColorSubject.next(font);

    this.timeoutRef = setTimeout(() => {
      this.closePopup();
    }, duration);
  }

  closePopup() {
    this.isPopupOpenSubject.next(false);

    if (this.timeoutRef) {
      clearTimeout(this.timeoutRef);
      this.timeoutRef = null;
    }
  }
}
