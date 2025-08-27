import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { NotificationService } from 'src/app/services/notification.service';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
   email = '';
  password = '';
  error: string | undefined;

  constructor(private authService: AuthService, private router: Router,private notify: NotificationService,private dialogRef: MatDialogRef<LoginComponent>) {}

onSubmit() {
  this.authService.login({ email: this.email, password: this.password }).subscribe(
    res => {
      alert('Login successful');

       /* ---------- NEW  —  open socket ---------- */
        if (res.userId) {                      // backend returns userId
          this.notify.connect(res.userId);     // <— ONE call per session
        }

      const role = res?.role;
      const userType = res?.userType;
      
      console.log('Role:', role);
      console.log('UserType:', userType);


      if (this.authService.isAdmin()) {
        this.router.navigate(['/admin-dashboard']);
      } else if (this.authService.isReporter() || this.authService.isReporterUserType()) {
        this.router.navigate(['/reporter-dashboard']);
      } else if (this.authService.isUser()) {
        if (this.authService.isFinder()) {
          this.router.navigate(['/finder-home']);
        } else if (this.authService.isLoser()) {
          this.router.navigate(['/loser-home']);
        } else {
          this.router.navigate(['/user-home']);
        }
      } else {
        this.router.navigate(['/login']); // fallback
      }
      this.dialogRef.close(); // ← close dialog here

    },
    err => {
      console.error(err);
      const errorMsg = err?.error?.message || err?.message || 'Unknown error';
      alert('Login failed: ' + errorMsg);
    }
  );
}


}
