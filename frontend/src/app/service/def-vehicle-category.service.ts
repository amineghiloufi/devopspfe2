import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { DefVehicleCategory } from '../model/DefVehicleCategory';
import { CustomHttpResponse } from '../model/custom-http-response';

@Injectable({ providedIn: 'root' })

export class DefVehicleCategoryService {

  private host = environment.apiUrl;
  
  constructor(private http: HttpClient) {}

  public getCategories(): Observable<DefVehicleCategory[]> {
    return this.http.get<DefVehicleCategory[]>(`${this.host}/categories`);
  }

  public getCategoriesOfTourism(): Observable<DefVehicleCategory[]> {
    return this.http.get<DefVehicleCategory[]>(`${this.host}/categories/ofTourism`);
  }

  public getCategoriesOfUtility(): Observable<DefVehicleCategory[]> {
    return this.http.get<DefVehicleCategory[]>(`${this.host}/categories/ofUtility`);
  }

  public addCategory(formData: FormData): Observable<DefVehicleCategory | HttpErrorResponse> {
    return this.http.post<DefVehicleCategory>(`${this.host}/categories/add`, formData);
  }

  public updateCategory(formData: FormData): Observable<DefVehicleCategory | HttpErrorResponse> {
    return this.http.put<DefVehicleCategory>(`${this.host}/categories/update`, formData);
  }

  public deleteCategory(categoryId: number): Observable<CustomHttpResponse | HttpErrorResponse> {
    return this.http.delete<any>(`${this.host}/categories/delete/${categoryId}`);
  }

  public createCategoryFormData(currentCategoryName: string, category: DefVehicleCategory): FormData {
    const formData = new FormData();
    formData.append('currentCategoryName', currentCategoryName);
    formData.append('categoryName', category.categoryName);
    formData.append('categoryType', category.categoryType);
    return formData;
  }
}