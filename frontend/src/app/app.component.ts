import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthService } from './auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  
  title = 'LostFoundProject';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      console.warn('Token expired or not found. Redirecting to login...');
      this.authService.logout();
    }

    // Handle unload-safe event
    window.addEventListener('pagehide', this.handlePageUnload);
    document.addEventListener('visibilitychange', this.handleVisibilityChange);
  }

  ngOnDestroy(): void {
    // Clean up listeners
    window.removeEventListener('pagehide', this.handlePageUnload);
    document.removeEventListener('visibilitychange', this.handleVisibilityChange);
  }

  handlePageUnload = () => {
    this.sendUnloadData();
  };

  handleVisibilityChange = () => {
    if (document.visibilityState === 'hidden') {
      this.sendUnloadData();
    }
  };

 sendUnloadData() {
  const userEmail = this.authService.getUserEmail?.(); // optional chaining
  const payload = JSON.stringify({
    time: new Date().toISOString(),
    user: userEmail || 'anonymous'
  });

  navigator.sendBeacon('http://localhost:8080/user/log-exit', payload);
}


}
