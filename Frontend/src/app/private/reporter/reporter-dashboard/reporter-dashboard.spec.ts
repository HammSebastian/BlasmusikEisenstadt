import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporterDashboard } from './reporter-dashboard';

describe('ReporterDashboard', () => {
  let component: ReporterDashboard;
  let fixture: ComponentFixture<ReporterDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReporterDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReporterDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
