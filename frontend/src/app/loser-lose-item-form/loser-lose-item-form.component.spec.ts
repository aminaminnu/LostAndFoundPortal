import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoserLoseItemFormComponent } from './loser-lose-item-form.component';

describe('LoserLoseItemFormComponent', () => {
  let component: LoserLoseItemFormComponent;
  let fixture: ComponentFixture<LoserLoseItemFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoserLoseItemFormComponent]
    });
    fixture = TestBed.createComponent(LoserLoseItemFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
