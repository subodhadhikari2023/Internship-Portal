import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { StudentNavComponent } from './student-nav.component';

describe('StudentNavComponent', () => {
  let component: StudentNavComponent;
  let fixture: ComponentFixture<StudentNavComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StudentNavComponent],
      imports: [HttpClientTestingModule, RouterTestingModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
