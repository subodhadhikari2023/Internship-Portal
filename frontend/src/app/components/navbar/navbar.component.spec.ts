import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { NavbarComponent } from './navbar.component';
import { UserService } from 'src/app/services/user.service';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;
  let userServiceSpy: jasmine.SpyObj<UserService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    userServiceSpy = jasmine.createSpyObj('UserService', [
      'fetchInstructorDetails',
      'loadProfilePicture',
    ]);
    routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);

    userServiceSpy.fetchInstructorDetails.and.returnValue(
      of({ name: 'Test Instructor', profilePictureFilePath: null })
    );

    await TestBed.configureTestingModule({
      declarations: [NavbarComponent],
      providers: [
        { provide: UserService, useValue: userServiceSpy },
        { provide: Router, useValue: routerSpy },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    localStorage.setItem('token', 'test.token');
  });

  afterEach(() => localStorage.clear());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('loads instructor details on init', () => {
    expect(userServiceSpy.fetchInstructorDetails).toHaveBeenCalled();
    expect(component.instructorData.name).toBe('Test Instructor');
  });

  describe('logOut()', () => {
    it('removes the token from localStorage', () => {
      component.logOut();
      expect(localStorage.getItem('token')).toBeNull();
    });

    it('navigates to /', () => {
      component.logOut();
      expect(routerSpy.navigateByUrl).toHaveBeenCalledWith('/');
    });
  });

  describe('toggleMenu()', () => {
    it('toggles menuOpen from false to true', () => {
      expect(component.menuOpen).toBeFalse();
      component.toggleMenu();
      expect(component.menuOpen).toBeTrue();
    });
  });

  describe('toggleDropdown()', () => {
    it('toggles the specified dropdown key', () => {
      expect(component.dropdowns['internships']).toBeFalse();
      component.toggleDropdown('internships');
      expect(component.dropdowns['internships']).toBeTrue();
      component.toggleDropdown('internships');
      expect(component.dropdowns['internships']).toBeFalse();
    });

    it('toggles independent keys independently', () => {
      component.toggleDropdown('applications');
      expect(component.dropdowns['internships']).toBeFalse();
      expect(component.dropdowns['applications']).toBeTrue();
    });
  });
});
