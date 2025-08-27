import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FounderFoundItemsComponent } from './founder-found-items.component';

describe('FounderFoundItemsComponent', () => {
  let component: FounderFoundItemsComponent;
  let fixture: ComponentFixture<FounderFoundItemsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FounderFoundItemsComponent]
    });
    fixture = TestBed.createComponent(FounderFoundItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
