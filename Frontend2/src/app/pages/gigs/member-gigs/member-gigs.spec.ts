import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberGigs } from './member-gigs';

describe('MemberGigs', () => {
  let component: MemberGigs;
  let fixture: ComponentFixture<MemberGigs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MemberGigs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MemberGigs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
