import { DefUser } from "./DefUser";
import { DefVehicleModel } from "./DefVehicleModel";

export class DefVehicle {

    public vehicleId: number;

    public user: DefUser;

    public model: DefVehicleModel;

    public registrationNumber: string;

    public color: string;

    public description: string;

    public price: number;

    public vehicleStatus: string;

  constructor() {
    this.vehicleId;
    this.user;
    this.model;
    this.registrationNumber;
    this.color;
    this.description;
    this.price;
    this.vehicleStatus;
  }
}