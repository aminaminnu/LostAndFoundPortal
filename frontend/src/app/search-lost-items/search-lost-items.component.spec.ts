import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchLostItemsComponent } from './search-lost-items.component';

describe('SearchLostItemsComponent', () => {
  let component: SearchLostItemsComponent;
  let fixture: ComponentFixture<SearchLostItemsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SearchLostItemsComponent]
    });
    fixture = TestBed.createComponent(SearchLostItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
