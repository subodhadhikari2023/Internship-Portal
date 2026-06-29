import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { UpdateProfileInstructorComponent } from './update-profile-instructor.component';

describe('UpdateProfileInstructorComponent', () => {
  let component: UpdateProfileInstructorComponent;
  let fixture: ComponentFixture<UpdateProfileInstructorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdateProfileInstructorComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule],
      schemas: [NO_ERRORS_SCHEMA],
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
