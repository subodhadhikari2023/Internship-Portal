import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { CreateInternshipComponent } from './create-internship.component';

describe('CreateInternshipComponent', () => {
  let component: CreateInternshipComponent;
  let fixture: ComponentFixture<CreateInternshipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreateInternshipComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateInternshipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
