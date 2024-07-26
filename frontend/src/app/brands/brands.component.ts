import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Subscription } from 'rxjs';
import { SubSink } from 'subsink';
import { NotificationType } from '../enum/notification-type.enum';
import { Role } from '../enum/role.enum';
import { DefBrand } from '../model/DefBrand';
import { CustomHttpResponse } from '../model/custom-http-response';
import { AuthenticationService } from '../service/authentication.service';
import { DefBrandService } from '../service/def-brand.service';
import { NotificationService } from '../service/notification.service';

@Component({
  selector: 'app-brands',
  templateUrl: './brands.component.html',
  styleUrls: ['./brands.component.css']
})
export class BrandsComponent implements OnInit, OnDestroy{
  
  public brands: DefBrand[];
  public editBrand: DefBrand;
  public currentBrandUpdateName = new DefBrand();
  public currentBrandMoveBrand = new DefBrand();
  public currentBrandAddToCategory = new DefBrand();
  public currentBrandRemoveFromCategory = new DefBrand();
  public newCategories: string[] = [''];
  public newBrandName: string;
  public currentCategoryName: string;
  public fromCategoryName: string;
  public toCategoryName: string;

  private subscriptions : Subscription [] = [] ;
  private subs = new SubSink;
  
  constructor(private defBrandService: DefBrandService, private notificationService: NotificationService,
              private authenticationService: AuthenticationService) {}
  
  ngOnInit(): void {
    this.getBrands(true);
  }

  public getBrands (showNotification : boolean ):void {
    this.subscriptions.push(
    this.defBrandService.getBrands().subscribe(
        (response : DefBrand[] ) => {
          this.brands = response ;
          if (showNotification) {
            this.sendNotification(NotificationType.SUCCESS, `${response.length} brand(s) loaded successfully.`);
          }
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message) ;
        }
      )
    );
  }

  onAddNewBrand(addNewBrandForm: NgForm): void {
    const formData = this.defBrandService.addBrandFormData(this.newBrandName, this.newCategories);
    this.subs.add(
      this.defBrandService.addBrand(formData).subscribe(
        (response: DefBrand) => {
          this.clickButton('new-brand-close');
          this.getBrands(false);
          addNewBrandForm.reset();
          this.sendNotification(NotificationType.SUCCESS , `The brand ${response.brandName} added successfully`);
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  addCategoryInput(): void {
    this.newCategories.push('');
  }

  trackByFn(index, category): number {
    return index;
  }
  

  public saveNewBrand(): void {
    this.clickButton('new-brand-save');
  }

  public onEditBrand(brand: DefBrand): void {
    this.editBrand = brand;
    this.clickButton('openBrandEdit');
  }

  public onEditBrandName(): void {
    this.currentBrandUpdateName = this.editBrand;
    this.clickButton('openBrandEditName');
  }

  public onUpdateBrandName(): void {
    const formData = this.defBrandService.updateBrandNameFormData(this.currentBrandUpdateName.brandName, this.newBrandName);
    this.subs.add(
      this.defBrandService.updateBrandName(formData).subscribe(
        (response: DefBrand) => {
          this.clickButton('closeUpdateBrandNameButton');
          this.getBrands(false);
          this.sendNotification(NotificationType.SUCCESS , `Brand's name is successfully updated.`);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  public onMoveBrandToCategory(): void {
    this.currentBrandMoveBrand = this.editBrand;
    this.clickButton('openMoveBrandModal');
  }

  public onMoveBrand(): void {
    const formData = this.defBrandService.moveBrandFormData(this.currentBrandMoveBrand.brandName, this.fromCategoryName, this.toCategoryName);
    this.subs.add(
      this.defBrandService.moveBrand(formData).subscribe(
        (response: DefBrand) => {
          this.clickButton('closeMoveBrandButton');
          this.getBrands(false);
          this.sendNotification(NotificationType.SUCCESS , `Brand is successfully moved to ${this.toCategoryName}.`);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  public onAddBrandToCategory(): void {
    this.currentBrandAddToCategory = this.editBrand;
    this.clickButton('openAddBrandToCategoryModal');
  }

  public onAddToCategory(): void {
    const formData = this.defBrandService.addToCategoryFormData(this.currentBrandAddToCategory.brandName, this.toCategoryName);
    this.subs.add(
      this.defBrandService.addToCategory(formData).subscribe(
        (response: DefBrand) => {
          this.clickButton('closeAddBrandToCategoryModal');
          this.getBrands(false);
          this.sendNotification(NotificationType.SUCCESS , `${response.brandName} is successfully added to ${this.toCategoryName}.`);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  public onRemoveBrandFromCategory(): void {
    this.currentBrandRemoveFromCategory = this.editBrand;
    this.clickButton('openRemoveBrandFromCategoryModal');
  }

  public onRemoveFromCategory(): void {
    const formData = this.defBrandService.removeFromCategoryFormData(this.currentBrandRemoveFromCategory.brandName, this.currentCategoryName);
    this.subs.add(
      this.defBrandService.removeFromCategory(formData).subscribe(
        (response: DefBrand) => {
          this.clickButton('closeRemoveBrandFromCategoryModal');
          this.getBrands(false);
          this.sendNotification(NotificationType.SUCCESS , `${response.brandName} is successfully removed from ${this.toCategoryName}.`);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  public onDeleteBrand(brandId: number): void {
    this.subs.add(
      this.defBrandService.deleteBrand(brandId).subscribe(
        (response: CustomHttpResponse) => {
          this.sendNotification(NotificationType.SUCCESS , response.message);
          this.getBrands(false);
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

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
}