import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ViewApplicationsComponent } from './view-applications.component';

describe('ViewApplicationsComponent', () => {
  let component: ViewApplicationsComponent;
  let fixture: ComponentFixture<ViewApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewApplicationsComponent],
      imports: [HttpClientTestingModule, RouterTestingModule],
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
