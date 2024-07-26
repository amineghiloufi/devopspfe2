import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { DefVehicle } from '../model/DefVehicle';
import { CustomHttpResponse } from '../model/custom-http-response';

@Injectable({ providedIn: 'root' })

export class DefVehicleService {

  private host = environment.apiUrl;
  
  constructor(private http: HttpClient) {}

  public getVehicles(): Observable<DefVehicle[]> {
    return this.http.get<DefVehicle[]>(`${this.host}/vehicles`);
  }

  public addVehicle(formData: FormData): Observable<DefVehicle | HttpErrorResponse> {
    return this.http.post<DefVehicle>(`${this.host}/vehicles/add`, formData);
  }

  public createVehicleFormData(agencyName: string, name: string, registrationNumber: string, price: number, color: string): FormData {
    const formData = new FormData();
    formData.append('agency', agencyName);
    formData.append('model', name);
    formData.append('registrationNumber', registrationNumber);
    formData.append('price', price.toString());
    formData.append('color', color);
    return formData;
  }

  public updateVehicleStatus(vehicleId: number, newStatus: string): Observable<DefVehicle | HttpErrorResponse> {
    return this.http.put<DefVehicle>(`${this.host}/vehicles/updateStatus/${vehicleId}?newStatus=${newStatus}`, null);
  }

  public updatePrice(formData: FormData): Observable<DefVehicle | HttpErrorResponse> {
    return this.http.put<DefVehicle>(`${this.host}/vehicles/updatePrice`, formData);
  }

  public updateVehiclePriceFormData(vehicle: DefVehicle): FormData {
    const formData = new FormData();
    formData.append('currentVehicle', JSON.stringify(vehicle.vehicleId));
    formData.append('price', JSON.stringify(vehicle.price));
    return formData;
  }

  public deleteVehicle(vehicleId: number): Observable<CustomHttpResponse | HttpErrorResponse> {
    return this.http.delete<any>(`${this.host}/vehicles/delete/${vehicleId}`);
  }
}