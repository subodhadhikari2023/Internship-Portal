import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ApplicationService } from './application.service';

const BASE = 'http://localhost:8080/internship-portal/api/v1/';

describe('ApplicationService', () => {
  let service: ApplicationService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(ApplicationService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getAllApplications()', () => {
    it('GETs student applications and maps entity', () => {
      const apps = [{ applicationId: 1 }];
      let result: any;
      service.getAllApplications().subscribe(r => (result = r));

      http.expectOne(`${BASE}students/view-submitted-applications`).flush({ entity: apps });
      expect(result).toEqual(apps);
    });
  });

  describe('getApplicationsForInstructor()', () => {
    it('GETs instructor applications and maps entity', () => {
      const apps = [{ applicationId: 2 }];
      let result: any;
      service.getApplicationsForInstructor().subscribe(r => (result = r));

      http.expectOne(`${BASE}instructors/applications`).flush({ entity: apps });
      expect(result).toEqual(apps);
    });
  });

  describe('setApplicationStatus()', () => {
    it('POSTs with applicationId as query param and status as body', () => {
      const app = { applicationId: 5 };
      const status = 'ACCEPTED';
      service.setApplicationStatus(app, status).subscribe();

      const mock = http.expectOne(
        `${BASE}instructors/review-applications?applicationId=5`
      );
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toBe(status);
      mock.flush({});
    });
  });

  describe('getNumberOfApplications()', () => {
    it('GETs pending application count and maps entity', () => {
      let result: any;
      service.getNumberOfApplications().subscribe(r => (result = r));

      http.expectOne(`${BASE}instructors/pending-applications`).flush({ entity: 7 });
      expect(result).toBe(7);
    });
  });

  describe('getRecentApplications()', () => {
    it('GETs recent applications and maps entity', () => {
      const recent = [{ applicationId: 9 }];
      let result: any;
      service.getRecentApplications().subscribe(r => (result = r));

      http.expectOne(`${BASE}instructors/recent-applications`).flush({ entity: recent });
      expect(result).toEqual(recent);
    });
  });
});
