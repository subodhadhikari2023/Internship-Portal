import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ForgetPasswordService } from './forget-password.service';

const BASE = 'http://localhost:8080/internship-portal/api/v1/';

describe('ForgetPasswordService', () => {
  let service: ForgetPasswordService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(ForgetPasswordService);
    http = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    http.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('findAccount()', () => {
    it('GETs with the email as a query param', () => {
      service.findAccount('user@test.com').subscribe();

      const mock = http.expectOne(`${BASE}public/forget-password?email=user@test.com`);
      expect(mock.request.method).toBe('GET');
      mock.flush({ entity: { userEmail: 'user@test.com' } });
    });

    it('maps the entity from the response', () => {
      let result: any;
      service.findAccount('user@test.com').subscribe(r => (result = r));

      http.expectOne(`${BASE}public/forget-password?email=user@test.com`)
        .flush({ entity: { userEmail: 'user@test.com' } });

      expect(result.userEmail).toBe('user@test.com');
    });
  });

  describe('getOTPForRegisteredAccount()', () => {
    it('GETs with email from localStorage', () => {
      localStorage.setItem('email', 'reset@test.com');
      service.getOTPForRegisteredAccount().subscribe();

      const mock = http.expectOne(`${BASE}public/get-password-change-otp?email=reset@test.com`);
      expect(mock.request.method).toBe('GET');
      mock.flush({ entity: { oneTimePassword: '123456' } });
    });
  });

  describe('submitOTP()', () => {
    it('POSTs the OTP request body', () => {
      const request = { oneTimePassword: '654321', userEmail: 'user@test.com' };
      service.submitOTP(request).subscribe();

      const mock = http.expectOne(`${BASE}public/validate-otp`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toEqual(request);
      mock.flush({ entity: true });
    });
  });

  describe('resetPassword()', () => {
    it('POSTs to reset-password with email from localStorage and new password', () => {
      localStorage.setItem('email', 'user@test.com');
      service.resetPassword('NewPass@1').subscribe();

      const mock = http.expectOne(
        `${BASE}public/reset-password?email=user@test.com&password=NewPass@1`
      );
      expect(mock.request.method).toBe('POST');
      mock.flush({});
    });
  });
});
