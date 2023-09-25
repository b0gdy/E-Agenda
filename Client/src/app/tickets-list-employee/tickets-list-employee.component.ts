import { Component, OnInit } from '@angular/core';
import {Ticket} from "../_models/ticket";
import {Status} from "../_models/status";
import {Priority} from "../_models/priority";
import {User} from "../_models/user";
import {Client} from "../_models/client";
import {TicketService} from "../_services/ticket.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {ClientService} from "../_services/client.service";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";

@Component({
  selector: 'app-tickets-list-employee',
  templateUrl: './tickets-list-employee.component.html',
  styleUrls: ['./tickets-list-employee.component.css']
})
export class TicketsListEmployeeComponent implements OnInit {

  page: number = 1;
  size: number = 5;
  total: number = 0;
  tickets: Ticket[] = [];
  date: Date = new Date();
  Complete = Status[Status.Complete];
  Working = Status[Status.Working];
  High = Priority[Priority.High];
  Low = Priority[Priority.Low];
  disableShown: boolean = false;
  model:any = {}
  user: User = {} as User;
  client: Client = {} as Client;
  unassignedShown: boolean = false;



  constructor(private ticketService: TicketService, private router: Router, private toastr: ToastrService, private clientService: ClientService, private userService: UserService) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if(user) {
          this.user = user;
        }
      }
    });
  }

  ngOnInit(): void {
    this.date = new Date(this.date.getFullYear() + '-' + (this.date.getMonth() + 1) + '-' + (this.date.getDate()))
    this.date.setHours(0);
    this.date.setMinutes(0);
    this.date.setSeconds(0);
    this.date.setMilliseconds(0);
    this.getByEmployeeAndEnabled(this.user.id);
  }

  getByEmployee(employeeId: number) {
    this.ticketService.getByEmployee(employeeId).subscribe({
      next: (response: Ticket[]) => {
        this.tickets = response;
        this.total = this.tickets.length;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  getByEmployeeAndEnabled(employeeId: number) {
    this.ticketService.getByEmployeeAndEnabled(employeeId).subscribe({
      next: (response: Ticket[]) => {
        this.tickets = response;
        this.total = this.tickets.length;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  showDisabled() {
    if(this.disableShown && this.unassignedShown) {
      this.disableShown = false;
      this.getByEmployeeAndEnabled(0);
    } else if(this.disableShown && !this.unassignedShown) {
      this.disableShown = false;
      this.getByEmployeeAndEnabled(this.user.id);
    } else if(!this.disableShown && this.unassignedShown) {
      this.disableShown = true;
      this.getByEmployee(0);
    } else if(!this.disableShown && !this.unassignedShown) {
      this.disableShown = true;
      this.getByEmployee(this.user.id);
    }
  }

  showUnassigned() {
    if(this.disableShown && this.unassignedShown) {
      this.unassignedShown = false;
      this.getByEmployee(this.user.id);
    } else if(this.disableShown && !this.unassignedShown) {
      this.unassignedShown = true;
      this.getByEmployee(0);
    } else if(!this.disableShown && this.unassignedShown) {
      this.unassignedShown = false;
      this.getByEmployeeAndEnabled(this.user.id);
    } else if(!this.disableShown && !this.unassignedShown) {
      this.unassignedShown = true;
      this.getByEmployeeAndEnabled(0);
    }
  }

}
