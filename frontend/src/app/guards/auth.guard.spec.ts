import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';

describe('AuthGuard', () => {
  let guard: AuthGuard;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['isAutenticated', 'getUserRole']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy },
      ]
    });
    guard = TestBed.inject(AuthGuard);
  });

  function routeWithRole(role: string): ActivatedRouteSnapshot {
    const snapshot = new ActivatedRouteSnapshot();
    (snapshot as any).data = { role };
    return snapshot;
  }

  it('allows access when authenticated and role matches', () => {
    authServiceSpy.isAutenticated.and.returnValue(true);
    authServiceSpy.getUserRole.and.returnValue('ROLE_STUDENT');

    const result = guard.canActivate(routeWithRole('ROLE_STUDENT'), {} as RouterStateSnapshot);

    expect(result).toBeTrue();
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });

  it('blocks and redirects when authenticated but role does not match', () => {
    authServiceSpy.isAutenticated.and.returnValue(true);
    authServiceSpy.getUserRole.and.returnValue('ROLE_STUDENT');

    const result = guard.canActivate(routeWithRole('ROLE_ADMIN'), {} as RouterStateSnapshot);

    expect(result).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/']);
  });

  it('blocks and redirects when not authenticated', () => {
    authServiceSpy.isAutenticated.and.returnValue(false);
    authServiceSpy.getUserRole.and.returnValue('');

    const result = guard.canActivate(routeWithRole('ROLE_INSTRUCTOR'), {} as RouterStateSnapshot);

    expect(result).toBeFalse();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/']);
  });

  it('allows ROLE_INSTRUCTOR with the correct role', () => {
    authServiceSpy.isAutenticated.and.returnValue(true);
    authServiceSpy.getUserRole.and.returnValue('ROLE_INSTRUCTOR');

    const result = guard.canActivate(routeWithRole('ROLE_INSTRUCTOR'), {} as RouterStateSnapshot);

    expect(result).toBeTrue();
  });

  it('allows ROLE_ADMIN with the correct role', () => {
    authServiceSpy.isAutenticated.and.returnValue(true);
    authServiceSpy.getUserRole.and.returnValue('ROLE_ADMIN');

    const result = guard.canActivate(routeWithRole('ROLE_ADMIN'), {} as RouterStateSnapshot);

    expect(result).toBeTrue();
  });
});
