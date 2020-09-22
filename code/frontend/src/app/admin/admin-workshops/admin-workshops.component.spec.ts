import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminWorkshopsComponent } from './admin-workshops.component';

describe('AdminWorkshopsComponent', () => {
  let component: AdminWorkshopsComponent;
  let fixture: ComponentFixture<AdminWorkshopsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminWorkshopsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminWorkshopsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
