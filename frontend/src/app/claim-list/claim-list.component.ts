import { Component, OnInit } from '@angular/core';
import { ClaimService, Claim } from '../services/claim.service';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-claim-list',
  templateUrl: './claim-list.component.html',
  styleUrls: ['./claim-list.component.css']
})
export class ClaimListComponent implements OnInit {
  claims: Claim[] = [];
  isLoading = true;
  error: string | null = null;
  isReporter = false;
  currentUserId: number | null = null;

  constructor(
    private claimService: ClaimService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.isReporter = this.auth.isReporter() || this.auth.isReporterUserType();
    this.currentUserId = this.auth.getUserId(); // ✅ assuming AuthService can get current user's ID
    this.fetchClaims();
  }

  fetchClaims(): void {
    this.isLoading = true;
    this.claimService.getAllClaims().subscribe({
      next: (data) => {
        // ✅ Add isFinder flag for UI
        this.claims = data.map(claim => ({
          ...claim,
          isFinder: claim.foundItemUserId === this.currentUserId
        }));
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to load claims.';
        this.isLoading = false;
      }
    });
  }

  trackById(index: number, claim: Claim): number | undefined {
    return claim.id;
  }

  approve(id: number) { 
    this.claimService.approveClaim(id).subscribe(() => this.fetchClaims()); 
  }

  reject(id: number) { 
    this.claimService.rejectClaim(id).subscribe(() => this.fetchClaims()); 
  }

  markPending(id: number) { 
    this.claimService.markPending(id).subscribe(() => this.fetchClaims()); 
  }

  askQuestion(id: number) { 
    this.claimService.askQuestion(id).subscribe(() => this.fetchClaims()); 
  }

  notifyLoser(id: number) { 
    this.claimService.notifyOwner(id).subscribe(() => this.fetchClaims()); 
  }

  notifyFinder(id: number) { 
    this.claimService.notifyFinder(id).subscribe(() => this.fetchClaims()); 
  }

  // ✅ Merged: Verify Answer & Ask Finder Permission
  verifyAnswer(id: number) { 
    this.claimService.verifyAnswerAndAskFinder(id).subscribe(() => this.fetchClaims()); 
  }

  // ✅ Finder Approve/Deny Contact
  setFinderPermission(id: number, allow: boolean) {
    this.claimService.setFinderPermission(id, allow).subscribe(() => this.fetchClaims());
  }

  shareFinderContact(id: number) { 
  this.claimService.shareFinderContact(id).subscribe((res) => {
    if (res?.finderEmail) {
      alert(`Finder's email: ${res.finderEmail}`);
    }
    this.fetchClaims();
  });
}


  openAnswerModal(claim: Claim) {
    const answer = prompt('Enter your answer to the reporter’s question:');
    if (answer) {
      this.claimService.submitLoserAnswer(claim.id!, answer).subscribe(() => {
        alert('Answer submitted successfully.');
        this.fetchClaims();
      });
    }
  }
}
