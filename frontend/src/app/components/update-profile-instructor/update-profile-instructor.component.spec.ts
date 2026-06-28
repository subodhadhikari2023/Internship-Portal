import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateProfileInstructorComponent } from './update-profile-instructor.component';

describe('UpdateProfileInstructorComponent', () => {
  let component: UpdateProfileInstructorComponent;
  let fixture: ComponentFixture<UpdateProfileInstructorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateProfileInstructorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateProfileInstructorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
