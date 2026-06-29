import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ForgetPasswordComponent } from './forget-password.component';
import { ForgetPasswordService } from 'src/app/services/forget-password.service';

describe('ForgetPasswordComponent', () => {
  let component: ForgetPasswordComponent;
  let fixture: ComponentFixture<ForgetPasswordComponent>;
  let serviceSpy: jasmine.SpyObj<ForgetPasswordService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    serviceSpy = jasmine.createSpyObj('ForgetPasswordService', [
      'findAccount',
      'getOTPForRegisteredAccount',
      'submitOTP',
      'resetPassword',
    ]);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [ForgetPasswordComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: ForgetPasswordService, useValue: serviceSpy },
        { provide: Router, useValue: routerSpy },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ForgetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    localStorage.clear();
  });

  afterEach(() => localStorage.clear());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('initializes with all three forms', () => {
    expect(component.forgetForm).toBeTruthy();
    expect(component.otpForm).toBeTruthy();
    expect(component.newPasswordForm).toBeTruthy();
  });

  describe('onSubmit() — find account', () => {
    it('sets accountFound=true and stores email on success', () => {
      component.forgetForm.setValue({ email: 'user@test.com' });
      serviceSpy.findAccount.and.returnValue(of({ userEmail: 'user@test.com' }));

      component.onSubmit();

      expect(component.accountFound).toBeTrue();
      expect(component.accountNotFound).toBeFalse();
      expect(localStorage.getItem('email')).toBe('user@test.com');
    });

    it('sets accountNotFound=true when service returns falsy', () => {
      component.forgetForm.setValue({ email: 'unknown@test.com' });
      serviceSpy.findAccount.and.returnValue(of(null));

      component.onSubmit();

      expect(component.accountNotFound).toBeTrue();
      expect(component.accountFound).toBeFalse();
    });
  });

  describe('claimAccount()', () => {
    it('sets accountClaimed=true and calls getOTPForRegisteredAccount()', () => {
      serviceSpy.getOTPForRegisteredAccount.and.returnValue(of({ oneTimePassword: '112233' }));

      component.claimAccount();

      expect(component.accountClaimed).toBeTrue();
      expect(serviceSpy.getOTPForRegisteredAccount).toHaveBeenCalled();
    });
  });

  describe('submitOtp()', () => {
    it('sets isOtpVerified=true and otpInvalid=false on valid OTP', () => {
      component.otpForm.setValue({ otp: '123456' });
      serviceSpy.submitOTP.and.returnValue(of(true));

      component.submitOtp();

      expect(component.isOtpVerified).toBeTrue();
      expect(component.otpInvalid).toBeFalse();
    });

    it('sets otpInvalid=true and isOtpVerified=false on error', () => {
      component.otpForm.setValue({ otp: '000000' });
      serviceSpy.submitOTP.and.returnValue(throwError(() => ({ status: 400 })));

      component.submitOtp();

      expect(component.otpInvalid).toBeTrue();
      expect(component.isOtpVerified).toBeFalse();
    });
  });

  describe('resetPassword()', () => {
    it('calls resetPassword and navigates to login on success', () => {
      component.newPasswordForm.setValue({ password: 'NewPass@1' });
      serviceSpy.resetPassword.and.returnValue(of({}));

      component.resetPassword();

      expect(serviceSpy.resetPassword).toHaveBeenCalledWith('NewPass@1');
      expect(routerSpy.navigate).toHaveBeenCalledWith(['login']);
    });
  });
});
