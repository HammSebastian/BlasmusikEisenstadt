import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GigDetail } from './gig-detail';

describe('GigDetail', () => {
  let component: GigDetail;
  let fixture: ComponentFixture<GigDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GigDetail]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GigDetail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
