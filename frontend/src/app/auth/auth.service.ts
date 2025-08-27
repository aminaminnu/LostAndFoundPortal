// src/app/auth/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { NotificationService } from '../services/notification.service';

type LoginResponse = {
  token: string;
  role: string | string[];           // allow API to return single role or array
  userType: string;                  // e.g. 'FINDER' | 'LOSER' | 'ADMIN' | 'REPORTER'
  userId?: number;
};

type JwtPayload = {
  sub?: string | number;
  id?: number | string;
  userId?: number | string;
  uid?: number | string;
  roles?: string[];                  // common claim
  authorities?: string[];            // spring often uses this
  scope?: string;                    // "ROLE_USER ROLE_ADMIN"
  role?: string | string[];          // some APIs put it here
  exp?: number;
  [k: string]: any;
};

@Injectable({ providedIn: 'root' })
export class AuthService {
  getUserEmail(): string | null {
  const decoded = this.safeDecode(this.getToken());
  return decoded?.['email'] || null;
}


  private tokenKey = 'authToken';
  private apiUrl   = 'http://localhost:8080';

  constructor(
    private http: HttpClient,
    private router: Router,
    private notify: NotificationService
  ) {}

  /* --------------- Auth API --------------- */

  login(credentials: { email: string; password: string }): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap((res) => {
        if (res.token) {
          this.saveToken(res.token);

          // Normalize and persist role(s)
          const roles = this.normalizeRoles(res.role);
          localStorage.setItem('userRole', roles[0] ?? ''); // keep first for compatibility
          localStorage.setItem('userRoles', JSON.stringify(roles));

          // Persist userType if your app uses it
          if (res.userType) localStorage.setItem('userType', res.userType);

          // Persist userId if sent by API
          if (typeof res.userId === 'number') {
            localStorage.setItem('userId', String(res.userId));
          } else {
            // fallback to decode from token
            const decoded = this.safeDecode(this.getToken());
            const uid = this.extractUserId(decoded);
            if (uid != null) localStorage.setItem('userId', String(uid));
          }

          // Open WS for in‑app notifications
          const uid = this.getUserId();
          if (uid) this.notify.connect(uid);
        }
      })
    );
  }

  register(payload: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/auth/register`, payload);
  }

  /* --------------- Token & storage --------------- */

  saveToken(token: string) { localStorage.setItem(this.tokenKey, token); }
  getToken(): string | null { return localStorage.getItem(this.tokenKey); }

  logout() {
    this.notify.disconnect();
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem('userRole');
    localStorage.removeItem('userRoles');
    localStorage.removeItem('userType');
    localStorage.removeItem('userId');
    this.router.navigate(['/login']);
  }

  /* --------------- Decode helpers --------------- */

  private safeDecode(token: string | null): JwtPayload | null {
    if (!token) return null;
    try { return jwtDecode<JwtPayload>(token); } catch { return null; }
  }

  private extractUserId(payload: JwtPayload | null): number | null {
    if (!payload) return null;
    const raw = payload.id ?? payload.userId ?? payload.uid ?? payload.sub ?? null;
    if (raw == null) return null;
    const n = typeof raw === 'string' ? Number(raw) : raw;
    return Number.isFinite(n) ? n : null;
  }

  private normalizeRoles(roleOrRoles: string | string[] | undefined): string[] {
    const arr = Array.isArray(roleOrRoles) ? roleOrRoles : (roleOrRoles ? [roleOrRoles] : []);
    // Also merge roles that may be inside the token
    const decoded = this.safeDecode(this.getToken());
    const tokenRoles: string[] = [];
    if (decoded?.roles) tokenRoles.push(...decoded.roles);
    if (decoded?.authorities) tokenRoles.push(...decoded.authorities);
    if (decoded?.role) tokenRoles.push(...(Array.isArray(decoded.role) ? decoded.role : [decoded.role]));
    if (decoded?.scope) tokenRoles.push(...decoded.scope.split(/\s+/));

    const all = [...arr, ...tokenRoles]
      .filter(Boolean)
      .map(r => r.toString().toUpperCase())
      .map(r => r.startsWith('ROLE_') ? r : `ROLE_${r}`);

    // de-duplicate
    return Array.from(new Set(all));
  }

  /* --------------- Session status --------------- */

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;
    const decoded = this.safeDecode(token);
    if (!decoded?.exp) return true; // if no exp, treat as logged in
    const now = Math.floor(Date.now() / 1000);
    return decoded.exp > now;
  }

  /* --------------- Identity getters --------------- */

  // Keep this for backward compatibility (you had a method with this name that threw)
  userId(): number | null { return this.getUserId(); }

  currentUserId(): number | null { return this.getUserId(); }

  getUserId(): number | null {
    const stored = localStorage.getItem('userId');
    if (stored) {
      const n = Number(stored);
      if (Number.isFinite(n)) return n;
    }
    // fallback to token
    const decoded = this.safeDecode(this.getToken());
    const uid = this.extractUserId(decoded);
    if (uid != null) localStorage.setItem('userId', String(uid));
    return uid;
  }

  getUserRole(): string | null { return localStorage.getItem('userRole'); }
  getUserRoles(): string[] {
    const raw = localStorage.getItem('userRoles');
    try { return raw ? JSON.parse(raw) : []; } catch { return []; }
  }

  getUserType(): string | null { return localStorage.getItem('userType'); }

  /* --------------- Role checks (robust) --------------- */

  private hasRole(target: string): boolean {
    const want = target.toUpperCase().startsWith('ROLE_') ? target.toUpperCase() : `ROLE_${target.toUpperCase()}`;
    return this.getUserRoles().includes(want);
  }

  isAdmin(): boolean    { return this.hasRole('ADMIN'); }
  isReporter(): boolean { return this.hasRole('REPORTER'); }
  isUser(): boolean     { return this.hasRole('USER'); }

  /* --------------- User type checks (optional) --------------- */
  isFinder(): boolean         { return this.getUserType() === 'FINDER'; }
  isLoser(): boolean          { return this.getUserType() === 'LOSER'; }
  isAdminUserType(): boolean  { return this.getUserType() === 'ADMIN'; }
  isReporterUserType(): boolean { return this.getUserType() === 'REPORTER'; }

  /* --------------- Optional: call on app bootstrap --------------- */
  // If you want to auto-reconnect notifications after a refresh:
  initSessionSideEffects() {
    if (this.isLoggedIn()) {
      const uid = this.getUserId();
      if (uid) this.notify.connect(uid);
    }
  }
}
