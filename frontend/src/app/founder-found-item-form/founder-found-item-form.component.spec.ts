import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FounderFoundItemFormComponent } from './founder-found-item-form.component';

describe('FounderFoundItemFormComponent', () => {
  let component: FounderFoundItemFormComponent;
  let fixture: ComponentFixture<FounderFoundItemFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FounderFoundItemFormComponent]
    });
    fixture = TestBed.createComponent(FounderFoundItemFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
