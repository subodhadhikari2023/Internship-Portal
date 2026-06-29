import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PopupComponent } from './popup.component';
import { NotificationService } from 'src/app/services/notification.service';
import { BehaviorSubject } from 'rxjs';

describe('PopupComponent', () => {
  let component: PopupComponent;
  let fixture: ComponentFixture<PopupComponent>;
  let notifierSpy: jasmine.SpyObj<NotificationService>;
  let isPopupOpen$: BehaviorSubject<boolean>;
  let message$: BehaviorSubject<string>;
  let color$: BehaviorSubject<string>;
  let fontColor$: BehaviorSubject<string>;

  beforeEach(async () => {
    isPopupOpen$ = new BehaviorSubject<boolean>(false);
    message$ = new BehaviorSubject<string>('');
    color$ = new BehaviorSubject<string>('');
    fontColor$ = new BehaviorSubject<string>('');

    notifierSpy = jasmine.createSpyObj('NotificationService', ['openPopup', 'closePopup'], {
      isPopupOpen$: isPopupOpen$.asObservable(),
      message$: message$.asObservable(),
      color$: color$.asObservable(),
      fontColor$: fontColor$.asObservable(),
    });

    await TestBed.configureTestingModule({
      declarations: [PopupComponent],
      providers: [{ provide: NotificationService, useValue: notifierSpy }]
    }).compileComponents();

    fixture = TestBed.createComponent(PopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('exposes isPopupOpen$ from NotificationService', () => {
    let open = false;
    component.isPopupOpen$.subscribe(v => (open = v));
    isPopupOpen$.next(true);
    expect(open).toBeTrue();
  });

  it('exposes message$ from NotificationService', () => {
    let msg = '';
    component.message$.subscribe(v => (msg = v));
    message$.next('Success!');
    expect(msg).toBe('Success!');
  });

  it('exposes color$ from NotificationService', () => {
    let color = '';
    component.color$.subscribe(v => (color = v));
    color$.next('#28a745');
    expect(color).toBe('#28a745');
  });

  it('exposes fontColor$ from NotificationService', () => {
    let font = '';
    component.fontColor$.subscribe(v => (font = v));
    fontColor$.next('#ffffff');
    expect(font).toBe('#ffffff');
  });
});
