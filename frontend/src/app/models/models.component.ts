import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Subscription } from 'rxjs';
import { SubSink } from 'subsink';
import { NotificationType } from '../enum/notification-type.enum';
import { DefVehicleModel } from '../model/DefVehicleModel';
import { CustomHttpResponse } from '../model/custom-http-response';
import { DefVehicleModelService } from '../service/def-vehicle-model.service';
import { NotificationService } from '../service/notification.service';

@Component({
  selector: 'app-models',
  templateUrl: './models.component.html',
  styleUrls: ['./models.component.css']
})
export class ModelsComponent implements OnInit, OnDestroy {
  
  public models: DefVehicleModel[];
  public model: DefVehicleModel;
  public brandName: string;
  public categoryName: string;
  public name: string;
  public version: string;
  public year: string;
  public horsePower: string;

  private subscriptions : Subscription [] = [] ;
  private subs = new SubSink;
  
  constructor(private notificationService: NotificationService, private defVehicleModelService: DefVehicleModelService) {}
  
  ngOnInit(): void {
    this.getModels(true);
  }

  public getModels (showNotification : boolean ):void {
    this.subscriptions.push(
    this.defVehicleModelService.getModels().subscribe(
        (response : DefVehicleModel[] ) => {
          this.models = response ;
          if (showNotification) {
            this.sendNotification(NotificationType.SUCCESS, `${response.length} model(s) loaded successfully.`);
          }
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message) ;
        }
      )
    );
  }

  public onAddNewModel(addNewModelForm: NgForm): void {
    const formData = this.defVehicleModelService.createModelFormData(this.brandName, this.categoryName, this.name, this.version, this.year, this.horsePower);
    this.subs.add(
      this.defVehicleModelService.addModel(formData).subscribe(
        (response: DefVehicleModel) => {
          this.clickButton('new-model-close');
          this.getModels(false);
          addNewModelForm.reset();
          this.sendNotification(NotificationType.SUCCESS , `${response.brand.brandName}'s model, ${response.name}, added successfully`);
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  public saveNewModel(): void {
    this.clickButton('new-model-save');
  }

  public deactivateModel(model: DefVehicleModel): void {
    const isCurrentlyActive = model.active;
    model.active = !model.active;
    this.defVehicleModelService.deactivatedModel(model.modelId).subscribe(
      (response: DefVehicleModel) => {
        this.getModels(false);
        if(isCurrentlyActive) {
          this.sendNotification(NotificationType.SUCCESS, `${response.name} activated successfully.`);
        } else {
          this.sendNotification(NotificationType.SUCCESS, `${response.name} deactivated successfully.`);
        }
      },
      (errorResponse: HttpErrorResponse) => {
        this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
      }
    );
  }

  public onDeleteModel(modelId: number): void {
    this.subs.add(
      this.defVehicleModelService.deleteModel(modelId).subscribe(
        (response: CustomHttpResponse) => {
          this.sendNotification(NotificationType.SUCCESS , response.message);
          this.getModels(false);
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

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
}