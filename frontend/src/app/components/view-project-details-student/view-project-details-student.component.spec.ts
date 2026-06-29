import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ViewProjectDetailsStudentComponent } from './view-project-details-student.component';

describe('ViewProjectDetailsStudentComponent', () => {
  let component: ViewProjectDetailsStudentComponent;
  let fixture: ComponentFixture<ViewProjectDetailsStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewProjectDetailsStudentComponent],
      imports: [HttpClientTestingModule, RouterTestingModule],
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
