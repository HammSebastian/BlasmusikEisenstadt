import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Gigs } from './gigs';

describe('Gigs', () => {
  let component: Gigs;
  let fixture: ComponentFixture<Gigs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Gigs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Gigs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
