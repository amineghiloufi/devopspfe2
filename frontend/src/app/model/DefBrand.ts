import { DefVehicleCategory } from "./DefVehicleCategory";
import { DefVehicleModel } from "./DefVehicleModel";

export class DefBrand {

    public brandId: number;

    public models: DefVehicleModel[];

    public categories: DefVehicleCategory[];

    public brandName: string;

  constructor() {
    this.brandId;
    this.models;
    this.categories;
    this.brandName;
  }
}