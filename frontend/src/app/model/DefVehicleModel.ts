import { DefBrand } from "./DefBrand";
import { DefVehicle } from "./DefVehicle";
import { DefVehicleCategory } from "./DefVehicleCategory";

export class DefVehicleModel {

    public modelId: number;

    public category: DefVehicleCategory;

    public brand: DefBrand;

    public vehicles: DefVehicle[];

    public name: string;

    public version: string;

    public year: string;

    public horsePower: string;

    public active: boolean;

  constructor() {
    this.modelId;
    this.category;
    this.brand;
    this.vehicles;
    this.name;
    this.version;
    this.year;
    this.horsePower;
    this.active;
  }
}