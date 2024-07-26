import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { DefBrand } from '../model/DefBrand';
import { CustomHttpResponse } from '../model/custom-http-response';

@Injectable({ providedIn: 'root' })

export class DefBrandService {

  private host = environment.apiUrl;
  
  constructor(private http: HttpClient) {}

  public getBrands(): Observable<DefBrand[]> {
    return this.http.get<DefBrand[]>(`${this.host}/brands`);
  }

  public addBrandFormData(brandName: string, categoryNames: string[]): FormData {
    const formData = new FormData();
    formData.append('brandName', brandName);
    for(const categoryName of categoryNames) {
      formData.append('categoryNames', categoryName);
    }
    return formData;
  }

  public addBrand(formData: FormData): Observable<DefBrand | HttpErrorResponse> {
    return this.http.post<DefBrand>(`${this.host}/brands/add`, formData);
  }

  public updateBrandNameFormData(currentBrandName: string, newBrandName: string): FormData {
    const formData = new FormData();
    formData.append('currentBrandName', currentBrandName);
    formData.append('newBrandName', newBrandName);
    return formData;
  }

  public updateBrandName(formData: FormData): Observable<DefBrand | HttpErrorResponse> {
    return this.http.put<DefBrand>(`${this.host}/brands/updateName`, formData);
  }

  public moveBrandFormData(currentBrandName: string, fromCategoryName: string, toCategoryName: string): FormData {
    const formData = new FormData();
    formData.append('currentBrandName', currentBrandName);
    formData.append('fromCategoryName', fromCategoryName);
    formData.append('toCategoryName', toCategoryName);
    return formData;
  }

  public moveBrand(formData: FormData): Observable<DefBrand | HttpErrorResponse> {
    return this.http.put<DefBrand>(`${this.host}/brands/move`, formData);
  }

  public addToCategoryFormData(currentBrandName: string, toCategoryName: string): FormData {
    const formData = new FormData();
    formData.append('currentBrandName', currentBrandName);
    formData.append('toCategoryName', toCategoryName);
    return formData;
  }

  public addToCategory(formData: FormData): Observable<DefBrand | HttpErrorResponse> {
    return this.http.put<DefBrand>(`${this.host}/brands/addToCategory`, formData);
  }

  public removeFromCategoryFormData(currentBrandName: string, currentCategoryName: string): FormData {
    const formData = new FormData();
    formData.append('currentBrandName', currentBrandName);
    formData.append('currentCategoryName', currentCategoryName);
    return formData;
  }

  public removeFromCategory(formData: FormData): Observable<DefBrand | HttpErrorResponse> {
    return this.http.put<DefBrand>(`${this.host}/brands/removeFromCategory`, formData);
  }

  public deleteBrand(brandId: number): Observable<CustomHttpResponse | HttpErrorResponse> {
    return this.http.delete<any>(`${this.host}/brands/delete/${brandId}`);
  }
}