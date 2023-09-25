import { Component, OnInit } from '@angular/core';
import {Ticket} from "../_models/ticket";
import {Status} from "../_models/status";
import {Priority} from "../_models/priority";
import {TicketService} from "../_services/ticket.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {User} from "../_models/user";
import {ClientService} from "../_services/client.service";
import {Client} from "../_models/client";

@Component({
  selector: 'app-tickets-list-client',
  templateUrl: './tickets-list-client.component.html',
  styleUrls: ['./tickets-list-client.component.css']
})
export class TicketsListClientComponent implements OnInit {

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
    this.disableShown = false;
    this.date = new Date(this.date.getFullYear() + '-' + (this.date.getMonth() + 1) + '-' + (this.date.getDate()))
    this.date.setHours(0);
    this.date.setMinutes(0);
    this.date.setSeconds(0);
    this.date.setMilliseconds(0);
    // this.getByClient(this.user.id);
    this.clientService.getById(this.user.id).subscribe({
      next: (response: Client) => {
        this.client = response;
        this.getByCompanyAndEnabled(this.client.company.id);
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  getByCompany(companyId: number) {
    this.ticketService.getByCompany(companyId).subscribe({
      next: (response: Ticket[]) => {
        this.tickets = response;
        this.total = this.tickets.length;
        this.disableShown = true;
      },
      error: err => {
        this.toastr.error(err.error);
        console.log("error = ", err.error)
      }
    })
  }

  getByTitleAndCompany(title: string, companyId: number) {
    this.ticketService.getByTitleAndCompany(title, companyId).subscribe({
      next: (response: Ticket[]) => {
        this.tickets = response;
        this.total = this.tickets.length;
        this.disableShown = true;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  getByCompanyAndEnabled(companyId: number) {
    this.ticketService.getByCompanyAndEnabled(companyId).subscribe({
      next: (response: Ticket[]) => {
        this.tickets = response;
        this.total = this.tickets.length;
        this.disableShown = false;
      },
      error: err => {
        this.toastr.error(err.error);
        console.log("error = ", err.error)
      }
    })
  }

  getByTitleAndCompanyAndEnabled(title: string, companyId: number) {
    this.ticketService.getByTitleAndCompanyAndEnabled(title, companyId).subscribe({
      next: (response: Ticket[]) => {
        this.tickets = response;
        this.total = this.tickets.length;
        this.disableShown = false;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  showDisabled() {
    if(this.disableShown) {
      this.getByCompanyAndEnabled(this.client.company.id)
      this.disableShown = false;
    } else {
      this.getByCompany(this.client.company.id)
      this.disableShown = true;
    }
  }

  searchByTitle(title: string) {
    if(title === undefined || title === "") {
      title = "no_title";
    }
    if(this.disableShown) {
      this.getByTitleAndCompany(title, this.client.company.id);
    } else {
      this.getByTitleAndCompanyAndEnabled(title, this.client.company.id);
    }
  }

}
