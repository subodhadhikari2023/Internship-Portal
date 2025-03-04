import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditInternshipComponent } from './edit-internship.component';

describe('EditInternshipComponent', () => {
  let component: EditInternshipComponent;
  let fixture: ComponentFixture<EditInternshipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditInternshipComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditInternshipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
