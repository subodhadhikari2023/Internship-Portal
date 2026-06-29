import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ViewApplicationComponent } from './view-application.component';

describe('ViewApplicationComponent', () => {
  let component: ViewApplicationComponent;
  let fixture: ComponentFixture<ViewApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewApplicationComponent],
      imports: [HttpClientTestingModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
