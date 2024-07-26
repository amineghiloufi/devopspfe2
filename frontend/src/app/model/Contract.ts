import { DefUser } from "./DefUser";
import { DefVehicle } from "./DefVehicle";
import { Invoice } from "./Invoice";

export class Contract {
    
    public contractId: number;
    
    public user: DefUser;
    
    public agencyName: string;
    
    public customerFullName: string;

    public vehicle: DefVehicle;

    public vehicleId: number;

    public vehicleDescription: String;

    public invoice: Invoice;

    public pickDateReservation: Date;

    public returnDateReservation: Date;

    public creationDate: Date;

    public price: number;

    public isSignedByAgency: boolean;

    public isSignedByCustomer: boolean;

    public active: boolean;

  constructor() {
    this.contractId;
    this.user;
    this.agencyName;
    this.customerFullName;
    this.vehicle;
    this.vehicleId;
    this.vehicleDescription;
    this.invoice;
    this.pickDateReservation;
    this.returnDateReservation;
    this.creationDate;
    this.price;
    this.isSignedByAgency;
    this.isSignedByCustomer;
    this.active;
  }
}