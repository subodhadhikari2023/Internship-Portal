import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { IntegrationService } from './integration.service';
import { LoginRequest } from '../models/login-request';

const BASE = 'http://localhost:8080/internship-portal/api/v1/public/';

describe('IntegrationService', () => {
  let service: IntegrationService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(IntegrationService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('doLogin()', () => {
    it('POSTs to the login endpoint with the request body', () => {
      const req = new LoginRequest();
      req.userEmail = 'a@b.com';
      req.userPassword = 'pass123';

      service.doLogin(req).subscribe();

      const mock = http.expectOne(`${BASE}login`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toEqual(req);
      mock.flush({ token: 'tok.en.val' });
    });

    it('returns the response token', () => {
      const req = new LoginRequest();
      let result: any;
      service.doLogin(req).subscribe(r => (result = r));

      http.expectOne(`${BASE}login`).flush({ token: 'my.jwt.token' });
      expect(result.token).toBe('my.jwt.token');
    });
  });

  describe('verifyOtp()', () => {
    it('POSTs to verify endpoint with email and otp as query params', () => {
      service.verifyOtp('a@b.com', '123456').subscribe();

      const mock = http.expectOne(`${BASE}register/verify?email=a@b.com&otp=123456`);
      expect(mock.request.method).toBe('POST');
      mock.flush({ entity: true });
    });
  });
});
