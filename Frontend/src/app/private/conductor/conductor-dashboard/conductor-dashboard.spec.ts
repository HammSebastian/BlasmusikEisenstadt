import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConductorDashboard } from './conductor-dashboard';

describe('ConductorDashboard', () => {
  let component: ConductorDashboard;
  let fixture: ComponentFixture<ConductorDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConductorDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConductorDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
