import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoserLoseItemsComponent } from './loser-lose-items.component';

describe('LoserLoseItemsComponent', () => {
  let component: LoserLoseItemsComponent;
  let fixture: ComponentFixture<LoserLoseItemsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoserLoseItemsComponent]
    });
    fixture = TestBed.createComponent(LoserLoseItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
