import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

 canActivate(route: ActivatedRouteSnapshot): boolean {
  const expectedRoles = route.data['roles'] as Array<string>;
  const expectedUserType = route.data['userType'];

  const userRole = this.authService.getUserRole();
  const userType = this.authService.getUserType();

  if (!this.authService.isLoggedIn()) {
    console.warn('Token expired. Redirecting to login...');
    this.authService.logout(); // logout already handles redirect
    return false;
  }

  if (!userRole || !expectedRoles.includes(userRole)) {
    this.router.navigate(['/login']);
    return false;
  }

  if (expectedUserType && userType !== expectedUserType) {
    this.router.navigate(['/login']);
    return false;
  }

  return true;
}


}
