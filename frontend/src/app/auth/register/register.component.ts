import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name: string = '';
  email: string = '';
  password: string = '';
  userType: string = '';  // e.g., admin, customer

  constructor(private authService: AuthService, private router: Router,private dialogRef: MatDialogRef<RegisterComponent> ) {}

  onSubmit() {
    const payload = {
      name: this.name,
      email: this.email,
      password: this.password,
      userType: this.userType
    };

    this.authService.register(payload).subscribe(
      (res: any) => {  // Use any or type properly if you have interface
        alert(res.message || 'Registration successful');
        this.dialogRef.close();
        this.router.navigate(['/login']);
      },
      (err: HttpErrorResponse) => {
        console.error(err);
        alert('Registration failed: ' + (err.error.error || err.message || 'Unknown error'));
      }
    );
  }
}
