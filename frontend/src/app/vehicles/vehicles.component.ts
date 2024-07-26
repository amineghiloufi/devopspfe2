import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Subscription } from 'rxjs';
import { SubSink } from 'subsink';
import { NotificationType } from '../enum/notification-type.enum';
import { DefVehicle } from '../model/DefVehicle';
import { CustomHttpResponse } from '../model/custom-http-response';
import { AuthenticationService } from '../service/authentication.service';
import { DefVehicleService } from '../service/def-vehicle.service';
import { NotificationService } from '../service/notification.service';

@Component({
  selector: 'app-vehicles',
  templateUrl: './vehicles.component.html',
  styleUrls: ['./vehicles.component.css']
})
export class VehiclesComponent implements OnInit, OnDestroy{
  
  public vehicles: DefVehicle[];
  public vehicle: DefVehicle;
  public agencyName: string;
  public name: string;
  public registrationNumber: string
  public price: number
  public color: string

  private subscriptions : Subscription [] = [] ;
  private subs = new SubSink;

  constructor(private notificationService: NotificationService, private defVehicleService: DefVehicleService,
              private authenticationService: AuthenticationService) {}
  
  ngOnInit(): void {
    this.getVehicles(true);
  }
  
  public getVehicles(showNotification: boolean): void {
    const currentLoggedInAgency = this.authenticationService.getUserFromLocalCache();
    if (currentLoggedInAgency) {
      this.subscriptions.push(
        this.defVehicleService.getVehicles().subscribe(
          (response: DefVehicle[]) => {
            this.vehicles = response.filter(vehicle =>vehicle.user.agencyName === currentLoggedInAgency.agencyName);
            if (showNotification) {
              this.sendNotification(NotificationType.SUCCESS, `${response.length} vehicle(s) loaded successfully.`);
            }
          },
          (errorResponse: HttpErrorResponse) => {
            this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
          }
        )
      );
    }
  }

  public filterVehiclesByStatus(vehicleStatus: string): void {
    this.vehicles = this.vehicles.filter(vehicle => vehicle.vehicleStatus === vehicleStatus);
  }

  public clearFilter(): void {
    this.getVehicles(false);
  }

  public onAddNewVehicle(addNewVehicleForm: NgForm): void {
    const formData = this.defVehicleService.createVehicleFormData(this.agencyName, this.name, this.registrationNumber, this.price, this.color);
    this.subs.add(
      this.defVehicleService.addVehicle(formData).subscribe(
        (response: DefVehicle) => {
          this.clickButton('new-vehicle-close');
          this.getVehicles(false);
          addNewVehicleForm.reset();
          this.sendNotification(NotificationType.SUCCESS , `Vehicle added successfully`);
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  public saveNewVehicle(): void {
    this.clickButton('new-vehicle-save');
  }

  public updateStatus(vehicle: DefVehicle, newStatus: string) {
    this.subs.add(
      this.defVehicleService.updateVehicleStatus(vehicle.vehicleId, newStatus).subscribe(
        (response: DefVehicle) => {
          this.sendNotification(NotificationType.SUCCESS , `Status set to ${response.vehicleStatus}.`);
          this.getVehicles(false);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }
  

  public onDeleteVehicle(vehicleId: number): void {
    this.subs.add(
      this.defVehicleService.deleteVehicle(vehicleId).subscribe(
        (response: CustomHttpResponse) => {
          this.sendNotification(NotificationType.SUCCESS , response.message);
          this.getVehicles(false);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
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

  getStatusColorClass(currentVehicleStatus: string): string {
    switch (currentVehicleStatus) {
      case 'Available':
        return 'status-available';
      case 'Rented':
        return 'status-rented';
      case 'InRepair':
        return 'status-in-repair';
      default:
        return '';
    }
  }

  getTextColor(status: string): string {
    switch (status) {
      case 'Available':
        return '#36a36c';
      case 'Rented':
        return '#1c9be6';
      case 'InRepair':
        return '#da2140';
      default:
        return '';
    }
  }
  
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
}
