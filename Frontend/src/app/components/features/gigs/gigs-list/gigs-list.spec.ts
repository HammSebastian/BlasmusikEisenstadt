import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GigsList } from './gigs-list';

describe('GigsList', () => {
  let component: GigsList;
  let fixture: ComponentFixture<GigsList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GigsList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GigsList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
