import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { HeaderType } from '../enum/header-type.enum';
import { NotificationType } from '../enum/notification-type.enum';
import { DefUser } from '../model/DefUser';
import { AuthenticationService } from '../service/authentication.service';
import { NotificationService } from '../service/notification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  public showLoading: boolean = false;
  private subscriptions: Subscription[] = [];

  constructor(private router: Router, private authenticationService: AuthenticationService,
              private notificationService: NotificationService) { }
  
  ngOnInit(): void {
      if (this.authenticationService.isUserLoggedIn()) {
        this.router.navigateByUrl('/home');
      } else {
        this.router.navigateByUrl('/login');
      }
  }

  public onLogin(user: DefUser): void {
    this.showLoading = true;
    this.subscriptions.push(
      this.authenticationService.login(user).subscribe(
        (response: HttpResponse<DefUser>) => {
          const token = response.headers.get(HeaderType.JWT_TOKEN);
          if (token) {
            this.authenticationService.saveToken(token);
          }
          if (response.body) {
            this.authenticationService.addUserToLocalCache(response.body);
          }
          this.router.navigateByUrl('/home');
          this.showLoading = false;
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendErrorNotification(NotificationType.ERROR, errorResponse.error.message);
          this.showLoading = false;
        }
      )
    );
  }

  private sendErrorNotification(notificationType: NotificationType, message: string): void {
    if (message) {
      this.notificationService.notify(notificationType, message);
    } else {
      this.notificationService.notify(notificationType, 'An error occured, please try again.');
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}