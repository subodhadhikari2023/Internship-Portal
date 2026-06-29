import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { BrowserModule } from '@angular/platform-browser';

const BASE = 'http://localhost:8080/internship-portal/api/v1/';

describe('UserService', () => {
  let service: UserService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BrowserModule]
    });
    service = TestBed.inject(UserService);
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

  describe('updatePassword()', () => {
    it('POSTs wrapped entity to common/reset-password', () => {
      service.updatePassword('NewPass@1').subscribe();
      const mock = http.expectOne(`${BASE}common/reset-password`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toEqual({ entity: 'NewPass@1' });
      mock.flush({ entity: true });
    });
  });

  describe('validatePassword()', () => {
    it('POSTs wrapped entity to common/validate-password', () => {
      service.validatePassword('OldPass@1').subscribe();
      const mock = http.expectOne(`${BASE}common/validate-password`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toEqual({ entity: 'OldPass@1' });
      mock.flush({ entity: true });
    });
  });

  describe('fetchAdminDetails()', () => {
    it('GETs admin profile and maps entity', () => {
      let result: any;
      service.fetchAdminDetails().subscribe(r => (result = r));
      http.expectOne(`${BASE}administrator/fetch-profile-details`).flush({ entity: { name: 'Admin' } });
      expect(result.name).toBe('Admin');
    });
  });

  describe('fetchInstructorDetails()', () => {
    it('GETs instructor profile and maps entity', () => {
      let result: any;
      service.fetchInstructorDetails().subscribe(r => (result = r));
      http.expectOne(`${BASE}instructors/get-profile-details`).flush({ entity: { name: 'Instructor' } });
      expect(result.name).toBe('Instructor');
    });
  });

  describe('fetchStudentDetails()', () => {
    it('GETs student profile and maps entity', () => {
      let result: any;
      service.fetchStudentDetails().subscribe(r => (result = r));
      http.expectOne(`${BASE}students/get-profile-details`).flush({ entity: { name: 'Student' } });
      expect(result.name).toBe('Student');
    });
  });

  describe('updateInstructorProfile()', () => {
    it('PUTs to instructors/update-profile', () => {
      const data = { name: 'Updated Instructor' };
      service.updateInstructorProfile(data).subscribe();
      const mock = http.expectOne(`${BASE}instructors/update-profile`);
      expect(mock.request.method).toBe('PUT');
      expect(mock.request.body).toEqual(data);
      mock.flush({ entity: data });
    });
  });

  describe('updateStudentProfile()', () => {
    it('PUTs to students/update-profile', () => {
      const data = { name: 'Updated Student' };
      service.updateStudentProfile(data).subscribe();
      const mock = http.expectOne(`${BASE}students/update-profile`);
      expect(mock.request.method).toBe('PUT');
      mock.flush({});
    });
  });

  describe('uploadProfilePicture()', () => {
    it('POSTs FormData to students/update-profile-picture', () => {
      const file = new File(['content'], 'pic.jpg', { type: 'image/jpeg' });
      service.uploadProfilePicture(file).subscribe();
      const mock = http.expectOne(`${BASE}students/update-profile-picture`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body instanceof FormData).toBeTrue();
      mock.flush({});
    });
  });

  describe('uploadInstructorProfilePicture()', () => {
    it('POSTs FormData to instructors/update-profile-picture', () => {
      const file = new File(['content'], 'pic.jpg', { type: 'image/jpeg' });
      service.uploadInstructorProfilePicture(file).subscribe();
      const mock = http.expectOne(`${BASE}instructors/update-profile-picture`);
      expect(mock.request.method).toBe('POST');
      mock.flush({});
    });
  });

  describe('uploadAdminProfilePicture()', () => {
    it('POSTs FormData to administrator/update-profile-picture', () => {
      const file = new File(['content'], 'pic.jpg', { type: 'image/jpeg' });
      service.uploadAdminProfilePicture(file).subscribe();
      const mock = http.expectOne(`${BASE}administrator/update-profile-picture`);
      expect(mock.request.method).toBe('POST');
      mock.flush({});
    });
  });

  describe('updateAdmin()', () => {
    it('PUTs to administrator/update-profile', () => {
      const data = { name: 'Admin Updated' };
      service.updateAdmin(data).subscribe();
      const mock = http.expectOne(`${BASE}administrator/update-profile`);
      expect(mock.request.method).toBe('PUT');
      mock.flush({ entity: data });
    });
  });

  describe('fetchStudentDetailsForInstructor()', () => {
    it('GETs with studentId as query param', () => {
      let result: any;
      service.fetchStudentDetailsForInstructor(99).subscribe(r => (result = r));
      http.expectOne(`${BASE}instructors/get-student-details?studentId=99`)
        .flush({ entity: { name: 'Some Student' } });
      expect(result.name).toBe('Some Student');
    });
  });

  describe('uploadResume()', () => {
    it('POSTs FormData to students/upload-resume', () => {
      const file = new File(['resume content'], 'resume.pdf', { type: 'application/pdf' });
      service.uploadResume(file).subscribe();
      const mock = http.expectOne(`${BASE}students/upload-resume`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body instanceof FormData).toBeTrue();
      mock.flush({ entity: 'path/to/resume.pdf' });
    });
  });
});
