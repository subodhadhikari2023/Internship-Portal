import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewInternshipStudentComponent } from './view-internship-student.component';

describe('ViewInternshipStudentComponent', () => {
  let component: ViewInternshipStudentComponent;
  let fixture: ComponentFixture<ViewInternshipStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewInternshipStudentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewInternshipStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
