import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { UpdateAdminComponent } from './update-admin.component';

describe('UpdateAdminComponent', () => {
  let component: UpdateAdminComponent;
  let fixture: ComponentFixture<UpdateAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdateAdminComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule, FormsModule],
      schemas: [NO_ERRORS_SCHEMA],
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
