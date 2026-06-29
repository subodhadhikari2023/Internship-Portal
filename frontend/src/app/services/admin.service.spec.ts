import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AdminService } from './admin.service';

const BASE = 'http://localhost:8080/internship-portal/api/v1/administrator/';

describe('AdminService', () => {
  let service: AdminService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(AdminService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getAllDepartments()', () => {
    it('GETs and maps entity array', () => {
      const depts = [{ id: 1, name: 'IT' }];
      let result: any;
      service.getAllDepartments().subscribe(r => (result = r));

      http.expectOne(`${BASE}department`).flush({ entity: depts });
      expect(result).toEqual(depts);
    });
  });

  describe('createNewDepartment()', () => {
    it('POSTs with wrapped entity and maps response', () => {
      let result: any;
      service.createNewDepartment('Finance').subscribe(r => (result = r));

      const mock = http.expectOne(`${BASE}department`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toEqual({ entity: 'Finance' });
      mock.flush({ entity: { id: 2, name: 'Finance' } });
      expect(result.name).toBe('Finance');
    });
  });

  describe('getAllUsers()', () => {
    it('GETs /users and maps entity', () => {
      let result: any;
      service.getAllUsers().subscribe(r => (result = r));
      http.expectOne(`${BASE}users`).flush({ entity: [{ id: 1 }] });
      expect(result).toEqual([{ id: 1 }]);
    });
  });

  describe('getAllStudents()', () => {
    it('GETs /students and maps entity', () => {
      let result: any;
      service.getAllStudents().subscribe(r => (result = r));
      http.expectOne(`${BASE}students`).flush({ entity: [{ id: 2 }] });
      expect(result).toEqual([{ id: 2 }]);
    });
  });

  describe('getAllInstructors()', () => {
    it('GETs /instructors and maps entity', () => {
      let result: any;
      service.getAllInstructors().subscribe(r => (result = r));
      http.expectOne(`${BASE}instructors`).flush({ entity: [{ id: 3 }] });
      expect(result).toEqual([{ id: 3 }]);
    });
  });

  describe('createInstructor()', () => {
    it('POSTs to /instructors with instructor data', () => {
      const instructor = { email: 'i@test.com', name: 'Jane' };
      service.createInstructor(instructor).subscribe();

      const mock = http.expectOne(`${BASE}instructors`);
      expect(mock.request.method).toBe('POST');
      expect(mock.request.body).toEqual(instructor);
      mock.flush({ entity: { id: 10, ...instructor } });
    });
  });

  describe('updateInstructor()', () => {
    it('PUTs to /instructors with updated data', () => {
      const updated = { id: 10, name: 'Jane Updated' };
      service.updateInstructor(updated).subscribe();

      const mock = http.expectOne(`${BASE}instructors`);
      expect(mock.request.method).toBe('PUT');
      expect(mock.request.body).toEqual(updated);
      mock.flush({ entity: updated });
    });
  });
});
