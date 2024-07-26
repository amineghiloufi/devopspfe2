import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { DefVehicleModel } from '../model/DefVehicleModel';
import { CustomHttpResponse } from '../model/custom-http-response';

@Injectable({ providedIn: 'root' })

export class DefVehicleModelService {

  private host = environment.apiUrl;
  
  constructor(private http: HttpClient) {}

  public getModels(): Observable<DefVehicleModel[]> {
    return this.http.get<DefVehicleModel[]>(`${this.host}/models`);
  }

  public addModel(formData: FormData): Observable<DefVehicleModel | HttpErrorResponse> {
    return this.http.post<DefVehicleModel>(`${this.host}/models/add`, formData);
  }

  public createModelFormData(brandName: string, categoryName: string, name: string, version: string, year: string, horsePower:string): FormData {
    const formData = new FormData();
    formData.append('brand', brandName);
    formData.append('category', categoryName);
    formData.append('modelName', name);
    formData.append('version', version);
    formData.append('year', year);
    formData.append('horsePower', horsePower);
    return formData;
  }

  public deactivatedModel(modelId: number): Observable<DefVehicleModel> {
  return this.http.put<DefVehicleModel>(`${this.host}/models/deactivation/${modelId}`, null);
}

  public deleteModel(modelId: number): Observable<CustomHttpResponse | HttpErrorResponse> {
    return this.http.delete<any>(`${this.host}/models/delete/${modelId}`);
  }
}