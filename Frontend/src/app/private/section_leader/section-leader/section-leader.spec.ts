import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SectionLeader } from './section-leader';

describe('SectionLeader', () => {
  let component: SectionLeader;
  let fixture: ComponentFixture<SectionLeader>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SectionLeader]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SectionLeader);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
