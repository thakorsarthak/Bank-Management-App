import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgetPinComponent } from './forget-pin.component';

describe('ForgetPinComponent', () => {
  let component: ForgetPinComponent;
  let fixture: ComponentFixture<ForgetPinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForgetPinComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ForgetPinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
