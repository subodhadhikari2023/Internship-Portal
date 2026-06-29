import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { StudentsHomeComponent } from './students-home.component';

describe('StudentsHomeComponent', () => {
  let component: StudentsHomeComponent;
  let fixture: ComponentFixture<StudentsHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StudentsHomeComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentsHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
