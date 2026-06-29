import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ViewEnrolledStudentsComponent } from './view-enrolled-students.component';

describe('ViewEnrolledStudentsComponent', () => {
  let component: ViewEnrolledStudentsComponent;
  let fixture: ComponentFixture<ViewEnrolledStudentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewEnrolledStudentsComponent],
      imports: [HttpClientTestingModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewEnrolledStudentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
