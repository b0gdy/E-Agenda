import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {TicketService} from "../_services/ticket.service";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {User} from "../_models/user";
import {Ticket} from "../_models/ticket";
import {Employee} from "../_models/employee";
import {Client} from "../_models/client";

@Component({
  selector: 'app-ticket-details-client',
  templateUrl: './ticket-details-client.component.html',
  styleUrls: ['./ticket-details-client.component.css']
})
export class TicketDetailsClientComponent implements OnInit {

  user: User = {} as User;
  ticketId: string = {} as string;
  ticket: Ticket = {} as Ticket;
  client: Client = {} as Client;
  ticketFetched: boolean = false;

  constructor(private route: ActivatedRoute, private ticketService: TicketService, private toastr: ToastrService, private userService: UserService) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if(user) {
          this.user = user;
        }
      }
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe({
      next: (params: Params) => {
        this.ticketId= params['id'];
        this.getById(this.ticketId);
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  getById(id: string) {
    this.ticketService.getById(id).subscribe({
      next: (response: any) => {
        this.ticket = response;
        this.ticketFetched = true;
        console.log("ticket = ", this.ticket)
        console.log("enabled = ", this.ticket.enabled)
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  disable(id: string) {
    this.ticketService.disable(id).subscribe({
      next: (response: any) => {
        this.ticket = response;
      },
      error: err => {
        this.toastr.error(err.error);
        console.log("error = ", err.error)
      },
      complete: () => {
        this.toastr.success("Ticket disabled!")
      }
    })
  }

}
