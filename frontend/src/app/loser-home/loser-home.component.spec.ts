import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoserHomeComponent } from './loser-home.component';

describe('LoserHomeComponent', () => {
  let component: LoserHomeComponent;
  let fixture: ComponentFixture<LoserHomeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoserHomeComponent]
    });
    fixture = TestBed.createComponent(LoserHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
