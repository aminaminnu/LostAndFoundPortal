import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoserDashboardComponent } from './loser-dashboard.component';

describe('LoserDashboardComponent', () => {
  let component: LoserDashboardComponent;
  let fixture: ComponentFixture<LoserDashboardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoserDashboardComponent]
    });
    fixture = TestBed.createComponent(LoserDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
