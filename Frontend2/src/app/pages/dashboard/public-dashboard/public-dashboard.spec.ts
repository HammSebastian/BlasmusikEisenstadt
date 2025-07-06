import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicDashboard } from './public-dashboard';

describe('PublicDashboard', () => {
  let component: PublicDashboard;
  let fixture: ComponentFixture<PublicDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
