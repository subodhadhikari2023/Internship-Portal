import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { InternshipService } from './internship.service';
import { InternshipCreationRequest } from '../models/internship-creation-request';

const BASE = 'http://localhost:8080/internship-portal/api/v1/';

describe('InternshipService', () => {
  let service: InternshipService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(InternshipService);
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

  describe('createInternship()', () => {
    it('POSTs to instructor internship endpoint', () => {
      const req = new InternshipCreationRequest();
      req.internshipName = 'Web Dev';
      service.createInternship(req).subscribe();

      const mock = http.expectOne(`${BASE}instructors/internship`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toEqual(req);
      mock.flush({});
    });
  });

  describe('getInternships()', () => {
    it('GETs and maps entity array', () => {
      const internships = [{ internshipId: 1, internshipName: 'Web Dev' }];
      let result: any;
      service.getInternships().subscribe(r => (result = r));

      http.expectOne(`${BASE}instructors/internship`).flush({ entity: internships });
      expect(result).toEqual(internships);
    });

    it('returns empty array when entity is null', () => {
      let result: any;
      service.getInternships().subscribe(r => (result = r));

      http.expectOne(`${BASE}instructors/internship`).flush({ entity: null });
      expect(result).toEqual([]);
    });
  });

  describe('updateInternship()', () => {
    it('PUTs to instructor internship endpoint', () => {
      const data = { internshipId: 1, internshipName: 'Updated' };
      service.updateInternship(data).subscribe();

      const mock = http.expectOne(`${BASE}instructors/internship`);
      expect(mock.request.method).toBe('PUT');
      mock.flush({});
    });
  });

  describe('applyForInternship()', () => {
    it('POSTs to student apply endpoint', () => {
      const req = { internshipId: 3 };
      service.applyForInternship(req).subscribe();

      const mock = http.expectOne(`${BASE}students/apply`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toEqual(req);
      mock.flush({});
    });
  });

  describe('hasStudentApplied()', () => {
    it('GETs with internship ID in path', () => {
      let result: any;
      service.hasStudentApplied(42).subscribe(r => (result = r));

      http.expectOne(`${BASE}students/has-applied/42`).flush({ entity: true });
      expect(result).toEqual({ entity: true });
    });
  });

  describe('createProject()', () => {
    it('POSTs to create-project and maps entity', () => {
      const req = { title: 'My Project', internshipStudentId: 1 };
      let result: any;
      service.createProject(req).subscribe(r => (result = r));

      const mock = http.expectOne(`${BASE}instructors/create-project`);
      expect(mock.request.method).toBe('POST');
      mock.flush({ entity: { projectId: 99 } });
      expect(result.projectId).toBe(99);
    });
  });

  describe('markProjectAsComplete()', () => {
    it('POSTs to change-project-status with the projectId in the path', () => {
      service.markProjectAsComplete({ projectId: 5, status: 'COMPLETED' }).subscribe();

      const mock = http.expectOne(`${BASE}instructors/change-project-status/5`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toBe('COMPLETED');
      mock.flush({});
    });
  });

  describe('claimCertificate()', () => {
    it('POSTs to generate-certificate with internshipStudentId', () => {
      service.claimCertificate(7).subscribe();

      const mock = http.expectOne(`${BASE}students/generate-certificate?internshipStudentId=7`);
      expect(mock.request.method).toBe('POST');
      mock.flush({ entity: 'cert-path.pdf' });
    });
  });

  describe('dashboard stats', () => {
    it('totalInternships() GETs and maps entity', () => {
      let result: any;
      service.totalInternships().subscribe(r => (result = r));
      http.expectOne(`${BASE}instructors/number-of-internships-created`).flush({ entity: 5 });
      expect(result).toBe(5);
    });

    it('totalApplications() GETs and maps entity', () => {
      let result: any;
      service.totalApplications().subscribe(r => (result = r));
      http.expectOne(`${BASE}instructors/total-applications-of-internships-created`).flush({ entity: 12 });
      expect(result).toBe(12);
    });

    it('totalActiveInternships() GETs and maps entity', () => {
      let result: any;
      service.totalActiveInternships().subscribe(r => (result = r));
      http.expectOne(`${BASE}instructors/active-internships`).flush({ entity: 3 });
      expect(result).toBe(3);
    });

    it('getFiveRecentInternships() GETs and maps entity', () => {
      const recent = [{ internshipId: 1 }];
      let result: any;
      service.getFiveRecentInternships().subscribe(r => (result = r));
      http.expectOne(`${BASE}instructors/recent-internships`).flush({ entity: recent });
      expect(result).toEqual(recent);
    });
  });
});
