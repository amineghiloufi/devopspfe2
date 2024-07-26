import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { SubSink } from 'subsink';
import { NotificationType } from '../enum/notification-type.enum';
import { Role } from '../enum/role.enum';
import { DefUser } from '../model/DefUser';
import { Invoice } from '../model/Invoice';
import { CustomHttpResponse } from '../model/custom-http-response';
import { AuthenticationService } from '../service/authentication.service';
import { InvoiceService } from '../service/invoice.service';
import { NotificationService } from '../service/notification.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-invoices',
  templateUrl: './invoices.component.html',
  styleUrls: ['./invoices.component.css']
})
export class InvoicesComponent implements OnInit, OnDestroy{

  public invoices: Invoice[];
  public invoice: Invoice;
  public user: DefUser;
  public selectedInvoice: Invoice;

  private subs = new SubSink();
  
  constructor(private invoiceService: InvoiceService, private notificationService: NotificationService,
              private authenticationService: AuthenticationService) {}
  
  ngOnInit(): void {
    this.getInvoices(true);
  }

  public getInvoices (showNotification : boolean ):void {
    this.subs.add(
    this.invoiceService.getInvoices().subscribe(
        (response : Invoice[] ) => {
          this.invoices  = response ;
          if (showNotification){
            this.sendNotification(NotificationType.SUCCESS, `${response.length} invoice(s) loaded successfully . `) ;
          }
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message) ;
        }
      )
    );
  }

  public onAddNewInvoice(invoiceForm: NgForm): void {
    const formData = this.invoiceService.createInvoiceFormData(invoiceForm.value);
    this.subs.add(
      this.invoiceService.addInvoice(formData).subscribe(
        (response: Invoice) => {
          this.clickButton('new-invoice-close');
          this.getInvoices(false);
          invoiceForm.reset();
          this.sendNotification(NotificationType.SUCCESS , `Invoice added successfully`) ;
        },
        (errorResponse : HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR , errorResponse.error.message) ;
        }
      )
    );
  }

  openAddInvoiceModal(): void {
    this.clickButton('addInvoiceModal');
  }

  public saveNewInvoice(): void {
    this.clickButton('new-invoice-save');
  }

  public onSelectInvoice(selectedInvoice: Invoice): void {
    this.selectedInvoice = selectedInvoice;
    this.clickButton('openInvoiceInfo');
  }

  public onDeleteInvoice(invoiceId: number): void {
    this.subs.add(
      this.invoiceService.deleteInvoice(invoiceId).subscribe(
        (response: CustomHttpResponse) => {
          this.sendNotification(NotificationType.SUCCESS , response.message);
          this.getInvoices(false);
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