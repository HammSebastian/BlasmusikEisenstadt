import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberGigsDetail } from './member-gigs-detail';

describe('MemberGigsDetail', () => {
  let component: MemberGigsDetail;
  let fixture: ComponentFixture<MemberGigsDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MemberGigsDetail]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MemberGigsDetail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
