import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderElementComponent } from './header-element.component';

describe('HeaderElementComponent', () => {
  let component: HeaderElementComponent;
  let fixture: ComponentFixture<HeaderElementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeaderElementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderElementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
