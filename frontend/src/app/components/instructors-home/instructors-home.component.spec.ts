import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { InstructorsHomeComponent } from './instructors-home.component';

describe('InstructorsHomeComponent', () => {
  let component: InstructorsHomeComponent;
  let fixture: ComponentFixture<InstructorsHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InstructorsHomeComponent],
      imports: [HttpClientTestingModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(InstructorsHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
