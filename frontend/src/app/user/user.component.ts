import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { SubSink } from 'subsink';
import { NotificationType } from '../enum/notification-type.enum';
import { Role } from '../enum/role.enum';
import { DefUser } from '../model/DefUser';
import { CustomHttpResponse } from '../model/custom-http-response';
import { AuthenticationService } from '../service/authentication.service';
import { NotificationService } from '../service/notification.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})

export class UserComponent implements OnInit, OnDestroy{

  private subs = new SubSink();
  private titleSubject = new BehaviorSubject<string>('Users');
  public titleAction$ = this.titleSubject.asObservable();
  public users: DefUser[];
  public user: DefUser;
  public refreshing: boolean;
  public selectedUser: DefUser;
  public editUser = new DefUser();
  private currentUser: string;

  constructor(private authenticationService: AuthenticationService ,private userService: UserService,
              private notificationService: NotificationService, private router: Router) {}
  
  ngOnInit(): void {
    this.user = this.authenticationService.getUserFromLocalCache();
    this.getUsers(true);
  }
  
  public changeTitle(title: string): void {
    this.titleSubject.next(title);
  }


  public getUsers (showNotification : boolean ):void {
    this.refreshing = true ;
    this.subs.add(
    this.userService.getUsers().subscribe(
        (response : DefUser[] ) => {
          this.userService.addUsersToLocalCache(response);
          this.users  = response ;
          this.refreshing = false ;
          if (showNotification){
            this.sendNotification(NotificationType.SUCCESS, `${response.length} user(s) loaded successfully . `) ;
          }
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message) ;
          this.refreshing = false ;
        }
      )
    );
  }

  public onSelectUser(selectedUser: DefUser): void {
    this.selectedUser = selectedUser;
    this.clickButton('openUserInfo');
  }
  
  public saveNewUser(): void {
    this.clickButton('new-user-save');
  }

  public onAddNewUser(userForm: NgForm): void {
    const formData = this.userService.createUserFormData(null, userForm.value);
    this.subs.add(
      this.userService.addUser(formData).subscribe(
        (response: DefUser) => {
          this.clickButton('new-user-close');
          this.getUsers(false);
          userForm.reset();
          this.sendNotification(NotificationType.SUCCESS , `${response.username} added successfully`) ;
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message) ;
        }
      )
    );
  }

  public onUpdateUser(): void {
    const formData = this.userService.createUserFormData(this.currentUser, this.editUser);
    this.subs.add(
      this.userService.updateUser(formData).subscribe(
        (response: DefUser) => {
          this.clickButton('closeEditUserModalButton');
          this.getUsers(false);
          this.sendNotification(NotificationType.SUCCESS , `${response.username} updated successfully`);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  public searchUsers(searchTerm: string): void {
    const results: DefUser[] = [];
    for (const user of this.userService.getUsersFromLocalCache()) {
      if (user.username.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1 ||
          user.email.toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1 ||
          user.userId.toString().toLowerCase().indexOf(searchTerm.toLowerCase()) !== -1 ) {
            results.push(user);
      }
    }
    this.users = results;
    if (results.length === 0 || !searchTerm) {
      this.users = this.userService.getUsersFromLocalCache();
    }
  }

  public onUpdateCurrentUser(user: DefUser): void {
    this.refreshing = true;
    this.currentUser = this.authenticationService.getUserFromLocalCache().username;
    const formData = this.userService.createUserFormData(this.currentUser, user);
    this.subs.add(
      this.userService.updateUser(formData).subscribe(
        (response: DefUser) => {
          this.authenticationService.addUserToLocalCache(response);
          this.getUsers(false);
          this.sendNotification(NotificationType.SUCCESS , `${response.username} updated successfully`) ;
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
          this.refreshing = true;
        }
      )
    );
  }

  public onLogOut(): void {
    this.authenticationService.logOut();
    this.router.navigate(['/home']);
    this.sendNotification(NotificationType.SUCCESS, `You've been successfully logged out`);
  }

  public onResetPassword(emailForm: NgForm): void {
    this.refreshing = true;
    const emailAddress = emailForm.value['reset-password-email'];
    this.subs.add(
      this.userService.resetPassword(emailAddress).subscribe(
        (response: CustomHttpResponse) => {
          this.sendNotification(NotificationType.SUCCESS , response.message);
          this.refreshing = false;
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.WARNING , errorResponse.error.message);
          this.refreshing= false;
        },
        () => emailForm.reset()
      )
    );
  }

  public onDeleteUser(username: string): void {
    this.subs.add(
      this.userService.deleteUser(username).subscribe(
        (response: CustomHttpResponse) => {
          this.sendNotification(NotificationType.SUCCESS , response.message);
          this.getUsers(false);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  public onEditUser(editUser: DefUser): void {
    this.editUser = editUser;
    this.currentUser = editUser.username;
    this.clickButton('openUserEdit');
  }
  
  public get isAgency() : boolean {
    return this.getUserRole() == Role.AGENCY;
  }

  public get isCustomer() : boolean {
    return this.getUserRole() == Role.CUSTOMER;
  }
  
  private getUserRole(): string {
    return this.authenticationService.getUserFromLocalCache().role;
  }

  private clickButton(buttonId: string): void {
    document.getElementById(buttonId).click();
  }

  private sendNotification(notificationType: NotificationType, message: string): void {
    if (message) {
      this.notificationService.notify(notificationType, message);
    } else {
      this.notificationService.notify(notificationType, 'An error occured, please try again.');
    }
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
}