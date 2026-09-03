import {Component, OnInit} from '@angular/core';
import {RouterLink, RouterLinkActive} from "@angular/router";
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {
    socketClient:any=null;
    private notificationSubscription: any;





  ngOnInit(): void {
    this.naviagetionHandler();

    const token = localStorage.getItem('token');

    console.log('TOKEN:', token);

    if (!token) {
      console.log('❌ No token');
      this.logout();
      return;
    }

    console.log('🔵 Creating SockJS connection...');

    const ws = new SockJS('http://localhost:8080/api/v1/ws');

    ws.onopen = () => {
      console.log('🟢 SOCKJS OPEN');
    };

    ws.onclose = (event) => {
      console.log('🔴 SOCKJS CLOSED', event);
    };

    ws.onerror = (error) => {
      console.error('🔴 SOCKJS ERROR', error);
    };

    this.socketClient = Stomp.over(ws);

    // مهم جدًا أثناء التشخيص
    this.socketClient.debug = (message: string) => {
      console.log('🟣 STOMP:', message);
    };

    this.socketClient.connect(
      {
        Authorization: `Bearer ${token}`
      },
      (frame: any) => {

        console.log('🟢 STOMP CONNECTED');
        console.log('FRAME:', frame);

        this.notificationSubscription =
          this.socketClient.subscribe(
            '/user/queue/notifications',
            (message: any) => {

              console.log(' NOTIFICATION RECEIVED');
              console.log('MESSAGE:', message);
              console.log('BODY:', message.body);

            }
          );

        console.log('🟢 SUBSCRIBED');
      },
      (error: any) => {
        console.error('🔴 STOMP ERROR:', error);
      }
    );
  }
  private getUserIdFronToken(): string {
    const token = localStorage.getItem('token');
    if (!token) {
      return '';
    }

    const payload = JSON.parse(atob(token.split('.')[1]));
    console.log(payload);
    return payload.sub;
  }
  private naviagetionHandler() {
    const  linkColor=document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if(window.location.href.endsWith(link.getAttribute('href')||'')){
        link.classList.add('active');
      }
      link.addEventListener('click',()=>{
        linkColor.forEach(l => l.classList.remove('active'));
        link.classList.add('active');
      })

    })
  }

  protected logout() {
    localStorage.clear();
    window.location.reload();

  }
}
