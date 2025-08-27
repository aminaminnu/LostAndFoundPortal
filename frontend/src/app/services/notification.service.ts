import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { BehaviorSubject } from 'rxjs';

/** Mirror of the payload the server pushes */
export interface NotificationPayload {
  receiverId: number;
  title:      string;
  message:    string;
  createdAt:  string;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  /** A list to hold all notifications */
  private notificationsList: NotificationPayload[] = [];

  /** Observable to stream list of notifications */
  private notificationsSubject = new BehaviorSubject<NotificationPayload[]>([]);
  readonly notifications$ = this.notificationsSubject.asObservable();

  private stomp: Client;

  constructor() {
    const sock = () => new SockJS('http://localhost:8080/ws');
    this.stomp = new Client({
      webSocketFactory : sock,
      reconnectDelay   : 5000
    });
  }

  connect(userId: number) {
    this.stomp.onConnect = () => {
      this.stomp.subscribe(`/topic/user/${userId}`, (msg: IMessage) => {
        const payload: NotificationPayload = JSON.parse(msg.body);
        this.notificationsList.unshift(payload); // add to top
        this.notificationsSubject.next(this.notificationsList); // update stream
      });
    };
    this.stomp.activate();
  }

  disconnect() {
    this.stomp.deactivate();
  }

  clearNotifications() {
    this.notificationsList = [];
    this.notificationsSubject.next(this.notificationsList);
  }
}
