import { ComponentFixture, TestBed, fakeAsync, tick, discardPeriodicTasks } from '@angular/core/testing';
import { LandingComponent } from './landing.component';

describe('LandingComponent', () => {
  let component: LandingComponent;
  let fixture: ComponentFixture<LandingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LandingComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(LandingComponent);
    component = fixture.componentInstance;
    // detectChanges() is called per-test so fakeAsync tests capture ngOnInit's interval
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('starts with first banner image (index 0)', () => {
    fixture.detectChanges();
    expect(component.currentImageIndex).toBe(0);
  });

  it('has a non-empty bannerImageUrls array', () => {
    expect(component.bannerImageUrls.length).toBeGreaterThan(0);
  });

  it('advances image index every 3 seconds', fakeAsync(() => {
    fixture.detectChanges(); // triggers ngOnInit inside fakeAsync
    tick(3000);
    expect(component.currentImageIndex).toBe(1);
    tick(3000);
    expect(component.currentImageIndex).toBe(2);
    discardPeriodicTasks();
  }));

  it('wraps image index back to 0 after last image', fakeAsync(() => {
    const total = component.bannerImageUrls.length;
    fixture.detectChanges();
    tick(3000 * total);
    expect(component.currentImageIndex).toBe(0);
    discardPeriodicTasks();
  }));

  describe('toggleMenu()', () => {
    it('toggles isMenuOpen from false to true', () => {
      fixture.detectChanges();
      expect(component.isMenuOpen).toBeFalse();
      component.toggleMenu();
      expect(component.isMenuOpen).toBeTrue();
    });

    it('toggles isMenuOpen back to false on second call', () => {
      fixture.detectChanges();
      component.toggleMenu();
      component.toggleMenu();
      expect(component.isMenuOpen).toBeFalse();
    });
  });

  describe('toggleNavbar()', () => {
    it('toggles isNavbarOpen from false to true', () => {
      fixture.detectChanges();
      expect(component.isNavbarOpen).toBeFalse();
      component.toggleNavbar();
      expect(component.isNavbarOpen).toBeTrue();
    });

    it('toggles isNavbarOpen back to false on second call', () => {
      fixture.detectChanges();
      component.toggleNavbar();
      component.toggleNavbar();
      expect(component.isNavbarOpen).toBeFalse();
    });
  });
});
