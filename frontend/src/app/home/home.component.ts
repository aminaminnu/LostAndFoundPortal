import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { LoginComponent } from '../auth/login/login.component';
import { RegisterComponent } from '../auth/register/register.component';
import { MatDialog } from '@angular/material/dialog';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  
  constructor(public authService: AuthService, private router: Router,private dialog: MatDialog  ) {}
  currentYear = new Date().getFullYear();

 openLoginModal(): void {
  this.dialog.open(LoginComponent, {
    width: '900px',
    maxWidth: '95vw',
    panelClass: 'custom-dialog-container'
  });
}


 openRegisterModal(): void {
  this.dialog.open(RegisterComponent, {
    width: '900px',
    maxWidth: '95vw',
    panelClass: 'custom-dialog-container'
  });
}


 ngOnInit(): void {
  
  const role = this.authService.getUserRole();
  const userType = this.authService.getUserType();

  console.log('Logged in role:', role);
  console.log('User type:', userType);

  if (this.authService.isAdmin()) {
    console.log("User is an ADMIN");
  } else if (this.authService.isReporter()) {
    if (this.authService.isFinder()) {
      console.log("User is a REPORTER for FOUND items");
    } else if (this.authService.isLoser()) {
      console.log("User is a REPORTER for LOST items");
    }
  } else if (this.authService.isUser()) {
    console.log("User is a regular USER");
  }
}


  logout() {
  this.authService.logout();
  this.router.navigate(['/login']);  // redirect after logout
}
}
