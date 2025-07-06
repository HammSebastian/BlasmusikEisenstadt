import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicGigs } from './public-gigs';

describe('PublicGigs', () => {
  let component: PublicGigs;
  let fixture: ComponentFixture<PublicGigs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicGigs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicGigs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
