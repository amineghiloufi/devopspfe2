import { Contract } from "./Contract";

export class Invoice {

    public invoiceId: number;

    public contract: Contract;

    public reference: string;

    public customerName: string;

    public creationDate : Date;

    public vehicleDescription: string;

    public totalPayment: string;

  constructor() {
    this.invoiceId;
    this.contract;
    this.reference;
    this.customerName;
    this.creationDate;
    this.vehicleDescription;
    this.totalPayment;
  }
}