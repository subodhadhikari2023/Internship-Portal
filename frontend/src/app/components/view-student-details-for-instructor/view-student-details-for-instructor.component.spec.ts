import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ViewStudentDetailsForInstructorComponent } from './view-student-details-for-instructor.component';

describe('ViewStudentDetailsForInstructorComponent', () => {
  let component: ViewStudentDetailsForInstructorComponent;
  let fixture: ComponentFixture<ViewStudentDetailsForInstructorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewStudentDetailsForInstructorComponent],
      imports: [HttpClientTestingModule, RouterTestingModule],
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
