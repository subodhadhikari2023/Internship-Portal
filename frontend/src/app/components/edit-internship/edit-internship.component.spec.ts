import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { EditInternshipComponent } from './edit-internship.component';

describe('EditInternshipComponent', () => {
  let component: EditInternshipComponent;
  let fixture: ComponentFixture<EditInternshipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditInternshipComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule],
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
