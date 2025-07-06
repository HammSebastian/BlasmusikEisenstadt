import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteMessages } from './site-messages';

describe('SiteMessages', () => {
  let component: SiteMessages;
  let fixture: ComponentFixture<SiteMessages>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SiteMessages]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SiteMessages);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
