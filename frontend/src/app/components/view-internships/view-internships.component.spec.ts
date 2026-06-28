import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewInternshipsComponent } from './view-internships.component';

describe('ViewInternshipsComponent', () => {
  let component: ViewInternshipsComponent;
  let fixture: ComponentFixture<ViewInternshipsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewInternshipsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewInternshipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
