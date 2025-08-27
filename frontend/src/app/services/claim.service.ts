import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Claim {
  id?: number;
  userId?: number;
  claimDate?: string;
  lostItemId?: number | null;
  foundItemId?: number | null;
  status?: 'PENDING' | 'APPROVED' | 'REJECTED';
  lostItem?: { itemName?: string };
  foundItem?: { itemName?: string };
  reporterQuestion?: string | null;
  loserAnswer?: string | null;
  verifiedByReporter?: boolean;
  finderGivesContactPermission?: boolean;
  foundItemUserId?: number | null;
  isFinder?: boolean; // <-- Added for frontend convenience
}

export interface ClaimDto {
  id: number;
  claimDate: string;
  userId: number;
  lostItemId: number | null;
  foundItemId: number | null;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  reporterQuestion?: string | null;
  loserAnswer?: string | null;
  verifiedByReporter?: boolean;
  finderGivesContactPermission?: boolean;
  foundItemUserId?: number | null;
}

@Injectable({ providedIn: 'root' })
export class ClaimService {
  private readonly base = 'http://localhost:8080/claims';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): { headers: HttpHeaders } {
    const token = localStorage.getItem('authToken');
    return {
      headers: new HttpHeaders().set('Authorization', `Bearer ${token}`)
    };
  }

  // ─── GET ─────────────────────────────
  getAllClaims(): Observable<Claim[]> {
    return this.http.get<Claim[]>(this.base, this.getAuthHeaders());
  }

  getMyClaims(): Observable<Claim[]> {
    return this.http.get<Claim[]>(`${this.base}/my-claims`, this.getAuthHeaders());
  }

  // ─── POST ────────────────────────────
  createClaim(body: { lostItemId: number | null; foundItemId: number | null }): Observable<Claim> {
    return this.http.post<Claim>(this.base, {
      lostItemId: body.lostItemId != null ? Number(body.lostItemId) : null,
      foundItemId: body.foundItemId != null ? Number(body.foundItemId) : null,
    }, this.getAuthHeaders());
  }

  askQuestion(id: number): Observable<any> {
    return this.http.post(`${this.base}/${id}/ask-question`, {}, this.getAuthHeaders());
  }

  notifyOwner(id: number): Observable<any> {
    return this.http.post(`${this.base}/${id}/notify-owner`, {}, this.getAuthHeaders());
  }

  notifyFinder(id: number): Observable<any> {
    return this.http.post(`${this.base}/${id}/notify-finder`, {}, this.getAuthHeaders());
  }

  submitLoserAnswer(id: number, answer: string): Observable<any> {
    return this.http.post(`${this.base}/${id}/answer`, { answer }, this.getAuthHeaders());
  }

  // ✅ Verify Answer & Ask Finder Permission (merged flow)
  verifyAnswerAndAskFinder(id: number): Observable<Claim> {
    return this.http.post<Claim>(
      `${this.base}/${id}/verify-and-ask-finder`,
      {},
      this.getAuthHeaders()
    );
  }

  // ✅ Share Finder Contact with claimant
  shareFinderContact(id: number): Observable<any> {
    return this.http.post(
      `${this.base}/${id}/share-finder-contact`,
      {},
      this.getAuthHeaders()
    );
  }

  // ─── PUT ─────────────────────────────
  approveClaim(id: number): Observable<Claim> {
    return this.http.put<Claim>(`${this.base}/${id}/approve`, {}, this.getAuthHeaders());
  }

  rejectClaim(id: number): Observable<Claim> {
    return this.http.put<Claim>(`${this.base}/${id}/reject`, {}, this.getAuthHeaders());
  }

  markPending(id: number): Observable<Claim> {
    return this.http.put<Claim>(`${this.base}/${id}/mark-pending`, {}, this.getAuthHeaders());
  }

  // ✅ Finder approves or denies contact sharing
  setFinderPermission(id: number, allow: boolean): Observable<Claim> {
    return this.http.put<Claim>(
      `${this.base}/${id}/finder-permission`,
      { allow },
      this.getAuthHeaders()
    );
  }
}
