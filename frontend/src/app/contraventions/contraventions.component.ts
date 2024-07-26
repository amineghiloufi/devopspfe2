import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Subscription } from 'rxjs';
import { SubSink } from 'subsink';
import { NotificationType } from '../enum/notification-type.enum';
import { Role } from '../enum/role.enum';
import { Contravention } from '../model/Contravention';
import { DefUser } from '../model/DefUser';
import { CustomHttpResponse } from '../model/custom-http-response';
import { AuthenticationService } from '../service/authentication.service';
import { ContraventionService } from '../service/contravention.service';
import { NotificationService } from '../service/notification.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-contraventions',
  templateUrl: './contraventions.component.html',
  styleUrls: ['./contraventions.component.css']
})
export class ContraventionsComponent implements OnInit{
  
  public contraventions: Contravention[] ;
  public contravention: Contravention;
  public users: DefUser[];
  public user: DefUser;
  public editContravention = new Contravention;
  public selectedContravention: Contravention;
  public description : string ;
  public customerFullName : string ;
  
  private currentContravention: number;
  private subscriptions : Subscription [] = [] ;
  private subs = new SubSink;
  
  constructor(private contraventionService: ContraventionService, private notificationService: NotificationService,
              private authenticationService: AuthenticationService, private userService: UserService) {}
  
  ngOnInit(): void {
    this.getContraventions(true);
  }

  public getContraventions (showNotification : boolean ):void {
    this.subscriptions.push(
    this.contraventionService.getContraventions().subscribe(
        (response : Contravention[] ) => {
          this.contraventions = response ;
          if (showNotification) {
            this.sendNotification(NotificationType.SUCCESS, `${response.length} contravention(s) loaded successfully.`);
          }
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message) ;
        }
      )
    );
  }

  public onAddNewContravention(contraventionForm: NgForm): void {
    const formData = this.contraventionService.createContraventionFormData(contraventionForm.value);
    this.subs.add(
      this.contraventionService.addContravention(formData).subscribe(
        (response: Contravention) => {
          this.clickButton('new-contravention-close');
          this.getContraventions(false);
          contraventionForm.reset();
          this.sendNotification(NotificationType.SUCCESS , `Contravention for ${response.customerFullName} added successfully`) ;
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message) ;
        }
      )
    );
  }

  public saveNewContravention(): void {
    this.clickButton('new-contravention-save');
  }

  openAddContraventionModal(): void {
    this.clickButton('addContraventionModal');
  }
  

  public onUpdateContravention(): void {
    const formData = this.contraventionService.updateContraventionFormData(this.currentContravention, this.editContravention);
    this.subs.add(
      this.contraventionService.updateContravention(formData).subscribe(
        (response: Contravention) => {
          this.clickButton('closeEditContraventionModalButton');
          this.getContraventions(false);
          this.sendNotification(NotificationType.SUCCESS , `Contravention N° ${response.contraventionId} updated
          successfully`);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  public onEditContravention(editContravention: Contravention): void {
    this.editContravention = editContravention;
    this.currentContravention = editContravention.contraventionId;
    this.clickButton('openContraventionEdit');
  }

  public onDeleteContravention(contraventionId: number): void {
    this.subs.add(
      this.contraventionService.deleteContravention(contraventionId).subscribe(
        (response: CustomHttpResponse) => {
          this.sendNotification(NotificationType.SUCCESS , response.message);
          this.getContraventions(false);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  private getUserRole(): string {
    return this.authenticationService.getUserFromLocalCache().role;
  }
  
  public get isAgency() : boolean {
    return this.getUserRole() == Role.AGENCY;
  }

  private sendNotification(notificationType: NotificationType, message: string): void {
    if (message) {
      this.notificationService.notify(notificationType, message);
    } else {
      this.notificationService.notify(notificationType, 'An error occured, please try again.');
    }
  }

  private clickButton(buttonId: string): void {
    document.getElementById(buttonId).click();
  }
  
}