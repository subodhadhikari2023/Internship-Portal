import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewStudentDetailsForInstructorComponent } from './view-student-details-for-instructor.component';

describe('ViewStudentDetailsForInstructorComponent', () => {
  let component: ViewStudentDetailsForInstructorComponent;
  let fixture: ComponentFixture<ViewStudentDetailsForInstructorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewStudentDetailsForInstructorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewStudentDetailsForInstructorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
