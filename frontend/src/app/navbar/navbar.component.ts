import { Component, OnInit } from '@angular/core';
import { Role } from '../enum/role.enum';
import { AuthenticationService } from '../service/authentication.service';
import { NotificationType } from '../enum/notification-type.enum';
import { NotificationService } from '../service/notification.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit{

  public isDroprightOpen: boolean = false;
  
  constructor(private authenticationService: AuthenticationService,
              private notificationService: NotificationService,
              private router: Router) {}
  
  ngOnInit(): void {
    const droprightButtons = document.querySelectorAll('.dropright button');
    droprightButtons.forEach(button => {
      button.addEventListener('click', (event: Event) => {
        event.stopPropagation();
        event.preventDefault();
        const dropdownMenu = button.nextElementSibling;
        if (dropdownMenu && !dropdownMenu.classList.contains('show')) {
          dropdownMenu.classList.add('show');
        } else {
          dropdownMenu?.classList.remove('show');
        }
      });
    });
  }

  public toggleDropright() {
    this.isDroprightOpen = !this.isDroprightOpen;
  }

  public isUserLoggedIn(): boolean {
    return this.authenticationService.isUserLoggedIn();
  }

  public onLogOut(): void {
    this.authenticationService.logOut();
    this.router.navigate(['/home']);
    this.sendNotification(NotificationType.SUCCESS, `You've been successfully logged out`);
  }

  private sendNotification(notificationType: NotificationType, message: string): void {
    if (message) {
      this.notificationService.notify(notificationType, message);
    } else {
      this.notificationService.notify(notificationType, 'An error occured, please try again.');
    }
  }
}
