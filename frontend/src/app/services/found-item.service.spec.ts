import { TestBed } from '@angular/core/testing';

import { FoundItemService } from './found-item.service';

describe('FoundItemService', () => {
  let service: FoundItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FoundItemService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
