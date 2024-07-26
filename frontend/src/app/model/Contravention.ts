import { DefUser } from "./DefUser";

export class Contravention {
    
    public contraventionId: number;

    public user: DefUser;

    public customerFullName: string;
    
    public contraventionDate: string;

    public contraventionDescription: string;

    public active: boolean;

  constructor() {
    this.contraventionId;
    this.user;
    this.customerFullName;
    this.contraventionDate;
    this.contraventionDescription;
    this.active;
  }
}