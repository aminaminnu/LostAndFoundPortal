import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NotificationService, NotificationPayload } from '../services/notification.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit, OnDestroy {
  open = false;
  unread = 0;
  list: NotificationPayload[] = [];
  sub: Subscription | undefined;

  constructor(private notificationService: NotificationService, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.sub = this.notificationService.notifications$.subscribe((notifications: NotificationPayload[]) => {
      this.list = notifications;
      this.unread = notifications.length;
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  toggle() {
    this.open = !this.open;
    if (this.open) this.unread = 0;
  }

  clear() {
    this.notificationService.clearNotifications();
  }
}
