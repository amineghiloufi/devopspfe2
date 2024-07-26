import { Contract } from "./Contract";
import { Contravention } from "./Contravention";
import { DefVehicle } from "./DefVehicle";

export class DefUser {
    
    public userId: number;

    public username: string;

    public email: string;

    public password: string;

    public customerFullName: string;

    public customerBirthDate: Date;

    public agencyName: string;

    public agencyAddress: string;

    public agencyOpeningHours: string;

    public phoneNumber: string;

    public isActive: boolean;

    public isNotLocked: boolean;

    public lastLoginDate: Date;

    public lastLoginDateDisplay: Date;

    public joinDate: Date;

    public role: string;

    public authorities: string[];

    public contracts: Contract[];

    public contraventions: Contravention[];

    public vehicles: DefVehicle[];

  constructor() {
    this.userId;
    this.username;
    this.email;
    this.password;
    this.customerFullName;
    this.customerBirthDate;
    this.agencyName;
    this.agencyAddress;
    this.agencyOpeningHours;
    this.phoneNumber;
    this.isActive;
    this.isNotLocked;
    this.lastLoginDate;
    this.lastLoginDateDisplay;
    this.joinDate;
    this.role;
    this.authorities;
    this.contracts;
    this.contraventions;
    this.vehicles;
  }
}