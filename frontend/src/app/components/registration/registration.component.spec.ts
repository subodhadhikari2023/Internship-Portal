import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { RegistrationComponent } from './registration.component';
import { RegistrationService } from 'src/app/services/registration.service';

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  let registrationSpy: jasmine.SpyObj<RegistrationService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    registrationSpy = jasmine.createSpyObj('RegistrationService', [
      'registerUser',
      'setRegisteredEmail',
    ]);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [RegistrationComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: RegistrationService, useValue: registrationSpy },
        { provide: Router, useValue: routerSpy },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('builds form with required controls', () => {
    ['userEmail', 'userPassword', 'userName', 'userPhoneNumber'].forEach(c =>
      expect(component.userForm.contains(c)).toBeTrue()
    );
  });

  it('form is invalid when empty', () => {
    expect(component.userForm.invalid).toBeTrue();
  });

  it('email control is invalid for a non-email string', () => {
    component.userForm.get('userEmail')!.setValue('notanemail');
    expect(component.userForm.get('userEmail')!.invalid).toBeTrue();
  });

  it('password control is invalid when it does not meet the pattern', () => {
    component.userForm.get('userPassword')!.setValue('simple');
    expect(component.userForm.get('userPassword')!.invalid).toBeTrue();
  });

  it('phone control is invalid for fewer than 10 digits', () => {
    component.userForm.get('userPhoneNumber')!.setValue('12345');
    expect(component.userForm.get('userPhoneNumber')!.invalid).toBeTrue();
  });

  describe('doRegistration()', () => {
    const validForm = {
      userEmail: 'user@test.com',
      userPassword: 'Valid@123',
      userName: 'Test User',
      userPhoneNumber: '9876543210',
    };

    it('marks all touched and does not call service when form is invalid', () => {
      spyOn(component.userForm, 'markAllAsTouched');
      component.doRegistration();
      expect(component.userForm.markAllAsTouched).toHaveBeenCalled();
      expect(registrationSpy.registerUser).not.toHaveBeenCalled();
    });

    it('calls registerUser with form values and navigates to /verifyOTP on success', () => {
      component.userForm.setValue(validForm);
      registrationSpy.registerUser.and.returnValue(
        of(new HttpResponse({ status: 200 }))
      );

      component.doRegistration();

      expect(registrationSpy.registerUser).toHaveBeenCalled();
      expect(registrationSpy.setRegisteredEmail).toHaveBeenCalledWith('user@test.com');
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/verifyOTP']);
    });

    it('sets 409 errorMessage when email already registered', () => {
      component.userForm.setValue(validForm);
      registrationSpy.registerUser.and.returnValue(throwError(() => ({ status: 409 })));

      component.doRegistration();

      expect(component.errorMessage).toBe(
        'Email already registered. Please use a different email.'
      );
    });

    it('sets generic errorMessage on other errors', () => {
      component.userForm.setValue(validForm);
      registrationSpy.registerUser.and.returnValue(throwError(() => ({ status: 500 })));

      component.doRegistration();

      expect(component.errorMessage).toBe('Registration Failed. Please try again.');
    });
  });
});
