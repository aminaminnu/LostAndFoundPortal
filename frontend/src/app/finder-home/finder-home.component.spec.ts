import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinderHomeComponent } from './finder-home.component';

describe('FinderHomeComponent', () => {
  let component: FinderHomeComponent;
  let fixture: ComponentFixture<FinderHomeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FinderHomeComponent]
    });
    fixture = TestBed.createComponent(FinderHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
