import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { CustomInterceptor } from './custom.interceptor';

describe('CustomInterceptor', () => {
  let http: HttpClient;
  let mock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: HTTP_INTERCEPTORS, useClass: CustomInterceptor, multi: true }
      ]
    });
    http = TestBed.inject(HttpClient);
    mock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    mock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    const interceptor = TestBed.inject<CustomInterceptor>(HTTP_INTERCEPTORS as any);
    expect(interceptor).toBeTruthy();
  });

  it('attaches Authorization header for a regular API call when token is present', () => {
    localStorage.setItem('token', 'test.jwt.token');
    http.get('/api/v1/students/data').subscribe();

    const req = mock.expectOne('/api/v1/students/data');
    expect(req.request.headers.get('Authorization')).toBe('Bearer test.jwt.token');
    req.flush({});
  });

  it('does NOT attach Authorization header for /login URL even when token is present', () => {
    localStorage.setItem('token', 'test.jwt.token');
    http.post('/api/v1/public/login', {}).subscribe();

    const req = mock.expectOne('/api/v1/public/login');
    expect(req.request.headers.has('Authorization')).toBeFalse();
    req.flush({});
  });

  it('does NOT attach Authorization header for /register URL even when token is present', () => {
    localStorage.setItem('token', 'test.jwt.token');
    http.post('/api/v1/public/register', {}).subscribe();

    const req = mock.expectOne('/api/v1/public/register');
    expect(req.request.headers.has('Authorization')).toBeFalse();
    req.flush({});
  });

  it('does NOT attach Authorization header when no token in localStorage', () => {
    http.get('/api/v1/students/data').subscribe();

    const req = mock.expectOne('/api/v1/students/data');
    expect(req.request.headers.has('Authorization')).toBeFalse();
    req.flush({});
  });

  it('passes the original request through unchanged when no token', () => {
    http.get('/api/v1/students/data').subscribe();

    const req = mock.expectOne('/api/v1/students/data');
    expect(req.request.method).toBe('GET');
    expect(req.request.url).toBe('/api/v1/students/data');
    req.flush({ entity: [] });
  });
});
