import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from './_modules/shared.module';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { UserComponent } from './user/user.component';
import { EmployeeComponent } from './employee/employee.component';
import { EmployeesListComponent } from './employees-list/employees-list.component';
import { EmployeeDetailsComponent } from './employee-details/employee-details.component';
import { EmployeeCreateComponent } from './employee-create/employee-create.component';
import { EmployeeUpdateComponent } from './employee-update/employee-update.component';
import { MeetingsListComponent } from './meetings-list/meetings-list.component';
import { MeetingDetailComponent } from './meeting-detail/meeting-detail.component';
import { MeetingCreateComponent } from './meeting-create/meeting-create.component';
import { TicketsListComponent } from './tickets-list/tickets-list.component';
import { MeetingsListEmployeeComponent } from './meetings-list-employee/meetings-list-employee.component';
import { MeetingDetailsEmployeeComponent } from './meeting-details-employee/meeting-details-employee.component';
import { TicketsListClientComponent } from './tickets-list-client/tickets-list-client.component';
import { TicketCreateClientComponent } from './ticket-create-client/ticket-create-client.component';
import { TicketsListEmployeeComponent } from './tickets-list-employee/tickets-list-employee.component';
import { TicketDetailsEmployeeComponent } from './ticket-details-employee/ticket-details-employee.component';
import { AccountActivatedComponent } from './account-activated/account-activated.component';
import { TicketDetailsClientComponent } from './ticket-details-client/ticket-details-client.component';
import { TicketUpdateClientComponent } from './ticket-update-client/ticket-update-client.component';
import { BusyTimesListEmployeeComponent } from './busy-times-list-employee/busy-times-list-employee.component';
import { BusyTimeDetailsEmployeeComponent } from './busy-time-details-employee/busy-time-details-employee.component';
import { BusyTimeCreateEmployeeComponent } from './busy-time-create-employee/busy-time-create-employee.component';
import { ClientsListComponent } from './clients-list/clients-list.component';
import { ClientDetailsComponent } from './client-details/client-details.component';
import { ClientCreateComponent } from './client-create/client-create.component';
import { ClientUpdateComponent } from './client-update/client-update.component';
import { TicketDetailsComponent } from './ticket-details/ticket-details.component';
import { CompaniesListComponent } from './companies-list/companies-list.component';
import { CompanyDetailsComponent } from './company-details/company-details.component';
import { CompanyCreateComponent } from './company-create/company-create.component';
import { CompanyUpdateComponent } from './company-update/company-update.component';
import { YourAccountComponent } from './your-account/your-account.component';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    HomeComponent,
    LoginComponent,
    UserComponent,
    EmployeeComponent,
    EmployeesListComponent,
    EmployeeDetailsComponent,
    EmployeeCreateComponent,
    EmployeeUpdateComponent,
    MeetingsListComponent,
    MeetingDetailComponent,
    MeetingCreateComponent,
    TicketsListComponent,
    MeetingsListEmployeeComponent,
    MeetingDetailsEmployeeComponent,
    TicketsListClientComponent,
    TicketCreateClientComponent,
    TicketsListEmployeeComponent,
    TicketDetailsEmployeeComponent,
    AccountActivatedComponent,
    TicketDetailsClientComponent,
    TicketUpdateClientComponent,
    BusyTimesListEmployeeComponent,
    BusyTimeDetailsEmployeeComponent,
    BusyTimeCreateEmployeeComponent,
    ClientsListComponent,
    ClientDetailsComponent,
    ClientCreateComponent,
    ClientUpdateComponent,
    TicketDetailsComponent,
    CompaniesListComponent,
    CompanyDetailsComponent,
    CompanyCreateComponent,
    CompanyUpdateComponent,
    YourAccountComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    SharedModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
