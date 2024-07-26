import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Contravention } from '../model/Contravention';
import { CustomHttpResponse } from '../model/custom-http-response';

@Injectable({ providedIn: 'root' })

export class ContraventionService {

  private host = environment.apiUrl;
  
  constructor(private http: HttpClient) {}

  public getContraventions(): Observable<Contravention[]> {
    return this.http.get<Contravention[]>(`${this.host}/contraventions`);
  }

  public addContravention(addFormData: FormData): Observable<Contravention | HttpErrorResponse> {
    return this.http.post<Contravention>(`${this.host}/contraventions/add`, addFormData);
  }

  public updateContravention(updateFormData: FormData): Observable<Contravention | HttpErrorResponse> {
    return this.http.put<Contravention>(`${this.host}/contraventions/update`, updateFormData);
  }

  public deleteContravention(contraventionId: number): Observable<CustomHttpResponse | HttpErrorResponse> {
    return this.http.delete<any>(`${this.host}/contraventions/delete/${contraventionId}`);
  }

  public createContraventionFormData(contravention :Contravention): FormData {
    const addFormData = new FormData();
    addFormData.append('customerFullName', contravention.customerFullName);
    addFormData.append('description', contravention.contraventionDescription);
    return addFormData;
  }

  public updateContraventionFormData(currentContraventionId: number, contravention: Contravention): FormData {
    const updateFormData = new FormData();
    updateFormData.append('currentContraventionId', JSON.stringify(currentContraventionId));
    updateFormData.append('active', JSON.stringify(contravention.active));
    return updateFormData;
  }
}