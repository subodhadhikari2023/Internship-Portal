import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewProjectDetailsStudentComponent } from './view-project-details-student.component';

describe('ViewProjectDetailsStudentComponent', () => {
  let component: ViewProjectDetailsStudentComponent;
  let fixture: ComponentFixture<ViewProjectDetailsStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewProjectDetailsStudentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewProjectDetailsStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
