import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AdminAuthGuard } from './_guards/admin-auth.guard';
import { EmployeeAuthGuard } from './_guards/employee-auth.guard';
import { UserComponent } from './user/user.component';
import {EmployeesListComponent} from "./employees-list/employees-list.component";
import {EmployeeDetailsComponent} from "./employee-details/employee-details.component";
import {EmployeeCreateComponent} from "./employee-create/employee-create.component";
import {EmployeeUpdateComponent} from "./employee-update/employee-update.component";
import {MeetingsListComponent} from "./meetings-list/meetings-list.component";
import {MeetingDetailComponent} from "./meeting-detail/meeting-detail.component";
import {MeetingCreateComponent} from "./meeting-create/meeting-create.component";
import {TicketsListComponent} from "./tickets-list/tickets-list.component";
import {MeetingsListEmployeeComponent} from "./meetings-list-employee/meetings-list-employee.component";
import {MeetingDetailsEmployeeComponent} from "./meeting-details-employee/meeting-details-employee.component";
import {ClientAuthGuard} from "./_guards/client-auth.guard";
import {UserAuthGuard} from "./_guards/user-auth.guard";
import {TicketsListClientComponent} from "./tickets-list-client/tickets-list-client.component";
import {TicketCreateClientComponent} from "./ticket-create-client/ticket-create-client.component";
import {TicketsListEmployeeComponent} from "./tickets-list-employee/tickets-list-employee.component";
import {TicketDetailsEmployeeComponent} from "./ticket-details-employee/ticket-details-employee.component";
import {AccountActivatedComponent} from "./account-activated/account-activated.component";
import {TicketDetailsClientComponent} from "./ticket-details-client/ticket-details-client.component";
import {TicketUpdateClientComponent} from "./ticket-update-client/ticket-update-client.component";
import {BusyTimesListEmployeeComponent} from "./busy-times-list-employee/busy-times-list-employee.component";
import {BusyTimeCreateEmployeeComponent} from "./busy-time-create-employee/busy-time-create-employee.component";
import {BusyTimeDetailsEmployeeComponent} from "./busy-time-details-employee/busy-time-details-employee.component";
import {ClientUpdateComponent} from "./client-update/client-update.component";
import {ClientsListComponent} from "./clients-list/clients-list.component";
import {ClientCreateComponent} from "./client-create/client-create.component";
import {ClientDetailsComponent} from "./client-details/client-details.component";
import {TicketDetailsComponent} from "./ticket-details/ticket-details.component";
import {CompaniesListComponent} from "./companies-list/companies-list.component";
import {CompanyCreateComponent} from "./company-create/company-create.component";
import {CompanyDetailsComponent} from "./company-details/company-details.component";
import {CompanyUpdateComponent} from "./company-update/company-update.component";
import {YourAccountComponent} from "./your-account/your-account.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {
    path: '',
    runGuardsAndResolvers: 'always',
    canActivate: [AdminAuthGuard],
    children: [
      // {path: 'admin-detail', component: AdminDetailComponent},
      {path: 'employees-list', component: EmployeesListComponent},
      {path: 'employee-create', component: EmployeeCreateComponent},
      {path: 'employee-details', children: [
          {path: ':id', component: EmployeeDetailsComponent},
        ]
      },
      {path: 'employee-update', children: [
          {path: ':id', component: EmployeeUpdateComponent},
        ]
      },
      {path: 'clients-list', component: ClientsListComponent},
      {path: 'client-create', component: ClientCreateComponent},
      {path: 'client-details', children: [
          {path: ':id', component: ClientDetailsComponent},
        ]
      },
      {path: 'client-update', children: [
          {path: ':id', component: ClientUpdateComponent},
        ]
      },
      {path: 'companies-list', component: CompaniesListComponent},
      {path: 'company-create', component: CompanyCreateComponent},
      {path: 'company-details', children: [
          {path: ':id', component: CompanyDetailsComponent},
        ]
      },
      {path: 'company-update', children: [
          {path: ':id', component: CompanyUpdateComponent},
        ]
      },
      {path: 'meetings-list', component: MeetingsListComponent},
      {path: 'meeting-create', component: MeetingCreateComponent},
      {path: 'meeting-details', children: [
          {path: ':id', component: MeetingDetailComponent},
        ]
      },
      {path: 'tickets-list', component: TicketsListComponent},
      {path: 'ticket-details', children: [
          {path: ':id', component: TicketDetailsComponent},
        ]
      },
    ]
  },
  {
    path: '',
    runGuardsAndResolvers: 'always',
    canActivate: [EmployeeAuthGuard],
    children: [
      {path: 'meetings-list-employee', component: MeetingsListEmployeeComponent},
      {path: 'meeting-details-employee', children: [
          {path: ':id', component: MeetingDetailsEmployeeComponent},
        ]
      },
      {path: 'busy-times-list-employee', component: BusyTimesListEmployeeComponent},
      {path: 'busy-time-create-employee', component: BusyTimeCreateEmployeeComponent},
      {path: 'busy-time-details-employee', children: [
          {path: ':id', component: BusyTimeDetailsEmployeeComponent},
        ]
      },
      {path: 'tickets-list-employee', component: TicketsListEmployeeComponent},
      {path: 'ticket-details-employee', children: [
          {path: ':id', component: TicketDetailsEmployeeComponent},
        ]
      },
    ]
  },
  {
    path: '',
    runGuardsAndResolvers: 'always',
    canActivate: [ClientAuthGuard],
    children: [
      {path: 'tickets-list-client', component: TicketsListClientComponent},
      {path: 'ticket-create-client', component: TicketCreateClientComponent},
      {path: 'ticket-details-client', children: [
          {path: ':id', component: TicketDetailsClientComponent},
        ]
      },
      {path: 'ticket-update-client', children: [
          {path: ':id', component: TicketUpdateClientComponent},
        ]
      },
    ]
  },
  {
    path: '',
    runGuardsAndResolvers: 'always',
    canActivate: [UserAuthGuard],
    children: [
      {path: 'user', component: UserComponent},
      {path: 'your-account', children: [
          {path: ':id', component: YourAccountComponent},
        ]
      },
    ]
  },
  {path: 'account-activated', children: [
      {path: ':username',  component:  AccountActivatedComponent},
    ],
  },
  {path: '**', component: HomeComponent, pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
