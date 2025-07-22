import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Youth } from './youth';

describe('Youth', () => {
  let component: Youth;
  let fixture: ComponentFixture<Youth>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Youth]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Youth);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
