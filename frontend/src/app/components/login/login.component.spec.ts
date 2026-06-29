import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { IntegrationService } from 'src/app/services/integration.service';
import { AuthService } from 'src/app/services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let integrationSpy: jasmine.SpyObj<IntegrationService>;
  let authSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    integrationSpy = jasmine.createSpyObj('IntegrationService', ['doLogin']);
    authSpy = jasmine.createSpyObj('AuthService', ['getUserRole']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: IntegrationService, useValue: integrationSpy },
        { provide: AuthService, useValue: authSpy },
        { provide: Router, useValue: routerSpy },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    localStorage.clear();
  });

  afterEach(() => localStorage.clear());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('builds the form with userEmail and userPassword controls', () => {
    expect(component.userForm.contains('userEmail')).toBeTrue();
    expect(component.userForm.contains('userPassword')).toBeTrue();
  });

  it('form is invalid when empty', () => {
    expect(component.userForm.invalid).toBeTrue();
  });

  it('form is valid with email and password of minimum length', () => {
    component.userForm.setValue({ userEmail: 'a@b.com', userPassword: 'pass' });
    expect(component.userForm.valid).toBeTrue();
  });

  describe('doLogin()', () => {
    it('does not call service when form is invalid', () => {
      spyOn(window, 'alert');
      component.userForm.setValue({ userEmail: '', userPassword: '' });
      component.doLogin();
      expect(integrationSpy.doLogin).not.toHaveBeenCalled();
    });

    it('navigates to /student for ROLE_STUDENT', () => {
      component.userForm.setValue({ userEmail: 'a@b.com', userPassword: 'pass' });
      integrationSpy.doLogin.and.returnValue(of({ token: 'jwt.stu.token' }));
      authSpy.getUserRole.and.returnValue('ROLE_STUDENT');

      component.doLogin();

      expect(localStorage.getItem('token')).toBe('jwt.stu.token');
      expect(routerSpy.navigate).toHaveBeenCalledWith(['/student']);
    });

    it('navigates to /instructor for ROLE_INSTRUCTOR', () => {
      component.userForm.setValue({ userEmail: 'a@b.com', userPassword: 'pass' });
      integrationSpy.doLogin.and.returnValue(of({ token: 'jwt.ins.token' }));
      authSpy.getUserRole.and.returnValue('ROLE_INSTRUCTOR');

      component.doLogin();

      expect(routerSpy.navigate).toHaveBeenCalledWith(['/instructor']);
    });

    it('navigates to /admin for ROLE_ADMIN', () => {
      component.userForm.setValue({ userEmail: 'a@b.com', userPassword: 'pass' });
      integrationSpy.doLogin.and.returnValue(of({ token: 'jwt.adm.token' }));
      authSpy.getUserRole.and.returnValue('ROLE_ADMIN');

      component.doLogin();

      expect(routerSpy.navigate).toHaveBeenCalledWith(['/admin']);
    });

    it('sets errorMessage on 401 error', () => {
      component.userForm.setValue({ userEmail: 'a@b.com', userPassword: 'pass' });
      integrationSpy.doLogin.and.returnValue(throwError(() => ({ status: 401 })));

      component.doLogin();

      expect(component.errorMessage).toBe('Invalid Credentials. Please try again!');
    });
  });
});
