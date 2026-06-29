import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';
import { OtpComponent } from './otp.component';
import { IntegrationService } from 'src/app/services/integration.service';
import { RegistrationService } from 'src/app/services/registration.service';

describe('OtpComponent', () => {
  let component: OtpComponent;
  let fixture: ComponentFixture<OtpComponent>;
  let integrationSpy: jasmine.SpyObj<IntegrationService>;
  let registrationSpy: jasmine.SpyObj<RegistrationService>;
  let routerSpy: jasmine.SpyObj<Router>;

  function configure(registeredEmail: string) {
    registrationSpy.getRegisteredEmail.and.returnValue(registeredEmail);

    TestBed.configureTestingModule({
      declarations: [OtpComponent],
      imports: [ReactiveFormsModule, HttpClientTestingModule],
      providers: [
        { provide: IntegrationService, useValue: integrationSpy },
        { provide: RegistrationService, useValue: registrationSpy },
        { provide: Router, useValue: routerSpy },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OtpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  beforeEach(() => {
    integrationSpy = jasmine.createSpyObj('IntegrationService', ['verifyOtp']);
    registrationSpy = jasmine.createSpyObj('RegistrationService', ['getRegisteredEmail']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
  });

  describe('when email is available', () => {
    beforeEach(async () => {
      await configure('user@test.com');
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('sets userEmail from RegistrationService', () => {
      expect(component.userEmail).toBe('user@test.com');
    });

    it('OTP control is invalid for non-6-digit input', () => {
      component.otpForm.get('otp')!.setValue('123');
      expect(component.otpForm.invalid).toBeTrue();
    });

    it('OTP control is valid for a 6-digit number', () => {
      component.otpForm.get('otp')!.setValue('654321');
      expect(component.otpForm.valid).toBeTrue();
    });

    describe('onSubmit()', () => {
      it('marks form touched and does not call service when form is invalid', () => {
        spyOn(component.otpForm, 'markAllAsTouched');
        component.onSubmit();
        expect(component.otpForm.markAllAsTouched).toHaveBeenCalled();
        expect(integrationSpy.verifyOtp).not.toHaveBeenCalled();
      });

      it('calls verifyOtp and navigates to /login on success', () => {
        component.otpForm.setValue({ otp: '123456' });
        integrationSpy.verifyOtp.and.returnValue(of({}));

        component.onSubmit();

        expect(integrationSpy.verifyOtp).toHaveBeenCalledWith('user@test.com', '123456');
        expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
      });

      it('sets errorMessage on OTP verification failure', () => {
        component.otpForm.setValue({ otp: '000000' });
        integrationSpy.verifyOtp.and.returnValue(throwError(() => ({ status: 400 })));

        component.onSubmit();

        expect(component.errorMessage).toBe('OTP verification failed. Please try again.');
      });
    });
  });

  describe('when no email is available', () => {
    beforeEach(async () => {
      await configure('');
    });

    it('redirects to /register when no email found', () => {
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/register']);
    });

    it('sets an error message', () => {
      expect(component.errorMessage).toBeTruthy();
    });
  });
});
