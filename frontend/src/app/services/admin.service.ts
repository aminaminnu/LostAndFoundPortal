/*  src/app/services/admin.service.ts  */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Claim {          // 👈  bring back if UI expects it
  id?: number;
  claimDate?: string;
  userId?: number;
  lostItemId?: number;
  foundItemId?: number;
  status?: string;
}

/* --- what the back‑end sends back --- */
export interface AppUser {
  id:        number;
  name:      string;
  email:     string;
  role:      'ROLE_ADMIN' | 'ROLE_REPORTER' | 'ROLE_USER';
  userType:  'FINDER' | 'LOSER' | 'REPORTER' | null;
  enabled:   boolean;        // optional: true / false
  // add more fields if your DTO exposes them (phone, createdAt …)
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  
  private readonly base = 'http://localhost:8080/admin/users';

  constructor(private http: HttpClient) {}

  /* ──────────────── CRUD ──────────────── */

  /** get *all* users (lightweight DTO) */
  listUsers(): Observable<AppUser[]> {
    return this.http.get<AppUser[]>(this.base);
  }

  /** update role, userType, enabled, … */
  updateUser(u: AppUser): Observable<AppUser> {
    return this.http.put<AppUser>(`${this.base}/${u.id}`, u);
  }

  /** delete user (cascade deletes their items/claims on the server) */
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  
}
