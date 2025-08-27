import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ClaimService } from '../services/claim.service';

@Component({
  selector: 'app-claim-form',
  templateUrl: './claim-form.component.html',
  styleUrls: ['./claim-form.component.css'],
})
export class ClaimFormComponent {

  // No `id` here!
  claim = {
    lostItemId: null as number | null,
    foundItemId: null as number | null,
  };

  constructor(
    private claimService: ClaimService,
    private router: Router,
    private snack: MatSnackBar
  ) {}

  submitClaim(): void {
    if (this.claim.lostItemId == null || this.claim.foundItemId == null) {
      this.snack.open('Both IDs are required', 'Close', { duration: 2500 });
      return;
    }

    this.claimService.createClaim({
      lostItemId: this.claim.lostItemId,
      foundItemId: this.claim.foundItemId
    }).subscribe({
      next: () => {
        this.snack.open('Claim submitted ✔', 'Close', { duration: 2500 });
        this.router.navigate(['/claim-list']);
      },
      error: (err) => {
        console.error('Claim submission failed:', err);
        const msg = err?.status === 401
          ? 'Unauthorized: please login again.'
          : 'Failed to submit claim ✖';
        this.snack.open(msg, 'Close', { duration: 3000 });
      },
    });
  }
}
