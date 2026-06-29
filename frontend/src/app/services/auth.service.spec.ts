import { TestBed, fakeAsync } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from './auth.service';

function makeToken(payload: object): string {
  const encode = (obj: object) =>
    btoa(JSON.stringify(obj)).replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
  return `${encode({ alg: 'HS256' })}.${encode(payload)}.sig`;
}

describe('AuthService', () => {
  let service: AuthService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [RouterTestingModule] });
    service = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    localStorage.clear();
  });

  afterEach(() => localStorage.clear());

  describe('isAutenticated()', () => {
    it('returns false when no token', () => {
      expect(service.isAutenticated()).toBeFalse();
    });

    it('returns true when token present', () => {
      localStorage.setItem('token', 'some.token.value');
      expect(service.isAutenticated()).toBeTrue();
    });
  });

  describe('getDecodedToken()', () => {
    it('returns null when no token', () => {
      expect(service.getDecodedToken()).toBeNull();
    });

    it('returns decoded payload when token present', () => {
      const payload = { sub: 'user@test.com', role: 'ROLE_STUDENT' };
      localStorage.setItem('token', makeToken(payload));
      const decoded = service.getDecodedToken();
      expect(decoded.sub).toBe('user@test.com');
      expect(decoded.role).toBe('ROLE_STUDENT');
    });
  });

  describe('getUserRole()', () => {
    it('returns empty string when no token', () => {
      expect(service.getUserRole()).toBe('');
    });

    it('returns role from decoded token', () => {
      localStorage.setItem('token', makeToken({ role: 'ROLE_INSTRUCTOR' }));
      expect(service.getUserRole()).toBe('ROLE_INSTRUCTOR');
    });
  });

  describe('logOut()', () => {
    it('removes token from localStorage', () => {
      localStorage.setItem('token', 'some.token');
      service.logOut();
      expect(localStorage.getItem('token')).toBeNull();
    });

    it('navigates to /login', fakeAsync(() => {
      const spy = spyOn(router, 'navigate');
      service.logOut();
      expect(spy).toHaveBeenCalledWith(['/login']);
    }));
  });
});
