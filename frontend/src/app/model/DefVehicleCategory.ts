import { DefBrand } from "./DefBrand";
import { DefVehicleModel } from "./DefVehicleModel";

export class DefVehicleCategory {

    public categoryId: number;

    public brands: DefBrand[];

    public models: DefVehicleModel[];

    public categoryName: string;

    public categoryType: string;

  constructor() {
    this.categoryId;
    this.brands;
    this.models;
    this.categoryName;
    this.categoryType;
  }
}