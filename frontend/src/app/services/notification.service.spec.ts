import { TestBed, fakeAsync, tick } from '@angular/core/testing';
import { NotificationService } from './notification.service';

describe('NotificationService', () => {
  let service: NotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('openPopup()', () => {
    it('emits correct message, color, and font color', () => {
      service.openPopup('Test message', '#fff', '#000', 5000);

      let msg = '';
      let color = '';
      let font = '';
      service.message$.subscribe(v => (msg = v));
      service.color$.subscribe(v => (color = v));
      service.fontColor$.subscribe(v => (font = v));

      expect(msg).toBe('Test message');
      expect(color).toBe('#fff');
      expect(font).toBe('#000');
    });

    it('sets isPopupOpen$ to true', () => {
      let open = false;
      service.isPopupOpen$.subscribe(v => (open = v));
      service.openPopup('hello', 'green', 'white', 3000);
      expect(open).toBeTrue();
    });

    it('auto-closes after the given duration', fakeAsync(() => {
      let open = false;
      service.isPopupOpen$.subscribe(v => (open = v));
      service.openPopup('msg', 'red', 'white', 2000);
      expect(open).toBeTrue();

      tick(2000);
      expect(open).toBeFalse();
    }));

    it('cancels previous timer when called again before it fires', fakeAsync(() => {
      let open = false;
      service.isPopupOpen$.subscribe(v => (open = v));

      service.openPopup('first', 'red', 'white', 5000);
      tick(3000);
      service.openPopup('second', 'blue', 'white', 4000);

      let msg = '';
      service.message$.subscribe(v => (msg = v));
      expect(msg).toBe('second');

      tick(4000);
      expect(open).toBeFalse();
    }));
  });

  describe('closePopup()', () => {
    it('sets isPopupOpen$ to false immediately', () => {
      let open = true;
      service.openPopup('msg', 'red', 'white', 5000);
      service.isPopupOpen$.subscribe(v => (open = v));

      service.closePopup();
      expect(open).toBeFalse();
    });
  });
});
