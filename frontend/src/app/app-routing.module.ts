import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { BrandsComponent } from "./brands/brands.component";
import { CategoriesComponent } from "./categories/categories.component";
import { ContraventionsComponent } from "./contraventions/contraventions.component";
import { HomeComponent } from "./home/home.component";
import { InvoicesComponent } from "./invoices/invoices.component";
import { LoginComponent } from "./login/login.component";
import { ModelsComponent } from "./models/models.component";
import { RegisterAgencyComponent } from "./register-agency/register-agency.component";
import { RegisterCustomerComponent } from "./register-customer/register-customer.component";
import { RegisterComponent } from "./register/register.component";
import { UserComponent } from "./user/user.component";
import { VehiclesComponent } from "./vehicles/vehicles.component";

const routes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'user/registerCustomer', component: RegisterCustomerComponent },
    { path: 'user/registerAgency', component: RegisterAgencyComponent },
    { path: 'user/management', component: UserComponent },
    { path: 'invoices', component: InvoicesComponent },
    { path: 'contraventions', component: ContraventionsComponent },
    { path: 'categories', component: CategoriesComponent },
    { path: 'brands', component: BrandsComponent },
    { path: 'models', component: ModelsComponent },
    { path: 'vehicles', component: VehiclesComponent },
    { path: '', redirectTo: '/home', pathMatch: 'full' }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRoutingModule {}