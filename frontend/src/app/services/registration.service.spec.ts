import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RegistrationService } from './registration.service';
import { RegistrationRequest } from '../models/registration-request';

const BASE = 'http://localhost:8080/internship-portal/api/v1/public';

describe('RegistrationService', () => {
  let service: RegistrationService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(RegistrationService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('registerUser()', () => {
    it('POSTs to /register with the registration request', () => {
      const req: RegistrationRequest = {
        userEmail: 'new@test.com',
        userPassword: 'Pass@1234',
        userName: 'Test User',
        userPhoneNumber: 9876543210,
      };

      service.registerUser(req).subscribe();

      const mock = http.expectOne(`${BASE}/register`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toEqual(req);
      mock.flush({});
    });
  });

  describe('setRegisteredEmail() / getRegisteredEmail()', () => {
    it('returns empty string initially', () => {
      expect(service.getRegisteredEmail()).toBe('');
    });

    it('stores and retrieves the email', () => {
      service.setRegisteredEmail('stored@test.com');
      expect(service.getRegisteredEmail()).toBe('stored@test.com');
    });

    it('overwrites a previously set email', () => {
      service.setRegisteredEmail('first@test.com');
      service.setRegisteredEmail('second@test.com');
      expect(service.getRegisteredEmail()).toBe('second@test.com');
    });
  });
});
