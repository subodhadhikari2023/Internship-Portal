import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { UpdateProfileStudentComponent } from './update-profile-student.component';

describe('UpdateProfileStudentComponent', () => {
  let component: UpdateProfileStudentComponent;
  let fixture: ComponentFixture<UpdateProfileStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdateProfileStudentComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule],
      schemas: [NO_ERRORS_SCHEMA],
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateProfileStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
