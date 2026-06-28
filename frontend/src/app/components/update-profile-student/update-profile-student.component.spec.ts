import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateProfileStudentComponent } from './update-profile-student.component';

describe('UpdateProfileStudentComponent', () => {
  let component: UpdateProfileStudentComponent;
  let fixture: ComponentFixture<UpdateProfileStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateProfileStudentComponent ]
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
