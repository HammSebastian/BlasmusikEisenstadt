import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GigsDetail } from './gigs-detail';

describe('GigsDetail', () => {
  let component: GigsDetail;
  let fixture: ComponentFixture<GigsDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GigsDetail]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GigsDetail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
