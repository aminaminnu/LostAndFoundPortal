import { TestBed } from '@angular/core/testing';

import { LoserService } from './loser.service';

describe('LoserService', () => {
  let service: LoserService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
