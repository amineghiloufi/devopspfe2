import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Contract } from '../model/Contract';

@Injectable({ providedIn: 'root' })

export class ContractService {

  private host = environment.apiUrl;
  
  constructor(private http: HttpClient) {}

  public getContracts(): Observable<Contract[]> {
    return this.http.get<Contract[]>(`${this.host}/contracts/list`);
  }

  public addContract(formData): Observable<Contract | HttpErrorResponse> {
    return this.http.post<Contract>(`${this.host}/contracts/add`, formData);
  }

  public updateContract(formData): Observable<Contract | HttpErrorResponse> {
    return this.http.post<Contract>(`${this.host}/contracts/update`, formData);
  }

  public deleteContract(contractId: number): Observable<Contract | HttpErrorResponse> {
    return this.http.delete<any>(`${this.host}/contracts/delete/${contractId}`);
  }

  public addContractsToLocalCache(contracts: Contract[]): void {
    localStorage.setItem('contracts', JSON.stringify(contracts));
  }

  public getContractsFromLocalCache() : Contract[] {
    if (localStorage.getItem('contracts')){
    return JSON.parse(localStorage.getItem('contracts')) ;
    }
  return null ;
  }

  public createContractFormData(contract: Contract): FormData {
    const formData = new FormData();
    formData.append('vehicleID', JSON.stringify(contract.vehicle.vehicleId));
    formData.append('customerFullName', contract.user.customerFullName);
    formData.append('pickDate', JSON.stringify(contract.pickDateReservation));
    formData.append('returnDate', JSON.stringify(contract.returnDateReservation));
    formData.append('isSignedByCustomer', JSON.stringify(contract.isSignedByCustomer));
    return formData;
  }
}