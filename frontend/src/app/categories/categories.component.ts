import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { map } from 'rxjs';
import { SubSink } from 'subsink';
import { CategoryType } from '../enum/CategoryType.enum';
import { NotificationType } from '../enum/notification-type.enum';
import { Role } from '../enum/role.enum';
import { DefVehicleCategory } from '../model/DefVehicleCategory';
import { CustomHttpResponse } from '../model/custom-http-response';
import { AuthenticationService } from '../service/authentication.service';
import { DefVehicleCategoryService } from '../service/def-vehicle-category.service';
import { NotificationService } from '../service/notification.service';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.css']
})
export class CategoriesComponent implements OnInit{

  public categories: DefVehicleCategory[];
  public category: DefVehicleCategory;
  public editCategory = new DefVehicleCategory();
  
  private currentCategory: string;
  private subs = new SubSink;
  
  constructor(private defVehicleCategoryService: DefVehicleCategoryService,
              private notificationService: NotificationService,
              private authenticationService: AuthenticationService,
              private route: ActivatedRoute) {}
  
  ngOnInit(): void {
    this.route.url.subscribe(urlSegments => {
      if (urlSegments.length > 1) {
        const categoryType = urlSegments[1].path;
        if (categoryType === 'ofTourism') {
          this.getCategoriesOfTypeTourism(true);
        } else if (categoryType === 'ofUtility') {
          this.getCategoriesOfTypeUtility(true);
        }
      } else {
        this.getCategories(true);
      }
    });
  }

  public getCategoriesOfTypeTourism(showNotification : boolean ):void {
    this.subs.add(
      this.defVehicleCategoryService.getCategoriesOfTourism()
        .pipe(
          map(categories => categories.filter(cats => cats.categoryType === JSON.stringify(CategoryType.Tourism)))
        ).subscribe(
          (filteredCategories: DefVehicleCategory[]) => {
            this.categories = filteredCategories;
            if (showNotification) {
              this.sendNotification(
                NotificationType.SUCCESS,
                `${filteredCategories.length} tourism category(ies) loaded`
              );
            }
          },
          (errorResponse) => {
            this.sendNotification(
              NotificationType.ERROR,
              errorResponse.error.message
            );
          }
      )
    );
  }

  public getCategoriesOfTypeUtility(showNotification : boolean ):void {
    this.subs.add(
      this.defVehicleCategoryService.getCategoriesOfUtility()
        .pipe(
          map(categories => categories.filter(cats => cats.categoryType === JSON.stringify(CategoryType.Utility)))
        ).subscribe(
          (filteredCategories: DefVehicleCategory[]) => {
            this.categories = filteredCategories;
            if (showNotification) {
              this.sendNotification(
                NotificationType.SUCCESS,
                `${filteredCategories.length} utility category(ies) loaded`
              );
            }
          },
          (errorResponse) => {
            this.sendNotification(
              NotificationType.ERROR,
              errorResponse.error.message
            );
          }
      )
    );
  }

  public getCategories (showNotification : boolean ):void {
    this.subs.add(
    this.defVehicleCategoryService.getCategories().subscribe(
        (response : DefVehicleCategory[] ) => {
          this.categories = response ;
          if (showNotification) {
            this.sendNotification(NotificationType.SUCCESS, `${response.length} category(ies) loaded successfully.`);
          }
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message) ;
        }
      )
    );
  }

  public onAddNewCategory(categoryForm: NgForm): void {
    const formData = this.defVehicleCategoryService.createCategoryFormData(null, categoryForm.value);
    this.subs.add(
      this.defVehicleCategoryService.addCategory(formData).subscribe(
        (response: DefVehicleCategory) => {
          this.clickButton('new-category-close');
          this.getCategories(false);
          categoryForm.reset();
          this.sendNotification(NotificationType.SUCCESS , `Category ${response.categoryName} added successfully`) ;
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message) ;
        }
      )
    );
  }

  public saveNewCategory(): void {
    this.clickButton('new-category-save');
  }

  openAddCategoryModal(): void {
    this.clickButton('addCategoryModal');
  }
  

  public onUpdateCategory(): void {
    const formData = this.defVehicleCategoryService.createCategoryFormData(this.currentCategory, this.editCategory);
    this.subs.add(
      this.defVehicleCategoryService.updateCategory(formData).subscribe(
        (response: DefVehicleCategory) => {
          this.clickButton('closeEditCategoryModalButton');
          this.getCategories(false);
          this.sendNotification(NotificationType.SUCCESS , `Category ${response.categoryName} updated successfully`);
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message);
        }
      )
    );
  }

  public onEditCategory(editCategory: DefVehicleCategory): void {
    this.editCategory = editCategory;
    this.currentCategory = editCategory.categoryName;
    this.clickButton('openCategoryEdit');
  }

  public onDeleteCategory(categoryId: number): void {
    this.subs.add(
      this.defVehicleCategoryService.deleteCategory(categoryId).subscribe(
        (response: CustomHttpResponse) => {
          this.sendNotification(NotificationType.SUCCESS , response.message);
          this.getCategories(false);
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