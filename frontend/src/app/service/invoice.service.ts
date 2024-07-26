import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Invoice } from '../model/Invoice';
import { CustomHttpResponse } from '../model/custom-http-response';

@Injectable({ providedIn: 'root' })

export class InvoiceService {

  private host = environment.apiUrl;
  
  constructor(private http: HttpClient) {}

  public getInvoices(): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.host}/invoices`);
  }

  public addInvoice(formData): Observable<Invoice | HttpErrorResponse> {
    return this.http.post<Invoice>(`${this.host}/invoices/add`, formData);
  }

  public deleteInvoice(invoiceId: number): Observable<CustomHttpResponse | HttpErrorResponse> {
    return this.http.delete<any>(`${this.host}/invoices/delete/${invoiceId}`);
  }

  public addInvoicesToLocalCache(invoice: Invoice[]): void {
    localStorage.setItem('invoice', JSON.stringify(invoice));
  }

  public getInvoicesFromLocalCache() : Invoice[] {
    if (localStorage.getItem('invoices')){
    return JSON.parse(localStorage.getItem('invoices')) ;
    }
  return null ;
  }

  public createInvoiceFormData(invoice: Invoice): FormData {
    const formData = new FormData();
    formData.append('invoiceId', JSON.stringify(invoice.invoiceId));
    formData.append('contract', JSON.stringify(invoice.contract));
    formData.append('reference', JSON.stringify(invoice.reference));
    formData.append('customerName', invoice.contract.customerFullName);
    formData.append('creationDate', JSON.stringify(invoice.creationDate));
    formData.append('vehicleDescription', invoice.contract.vehicle.description);
    formData.append('totalPayment', JSON.stringify(invoice.contract.price));
    return formData;
  }
}