import { Component, OnInit } from '@angular/core';
import {User} from "../_models/user";
import {Ticket} from "../_models/ticket";
import {Client} from "../_models/client";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {TicketService} from "../_services/ticket.service";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {NgForm} from "@angular/forms";
import {Priority} from "../_models/priority";
import {Description} from "../_models/description";

@Component({
  selector: 'app-ticket-update-client',
  templateUrl: './ticket-update-client.component.html',
  styleUrls: ['./ticket-update-client.component.css']
})
export class TicketUpdateClientComponent implements OnInit {

  user: User = {} as User;
  ticketId: string = {} as string;
  ticket: Ticket = {} as Ticket;
  client: Client = {} as Client;
  ticketFetched: boolean = false;
  model:any = {}
  model_aux:any = {}
  Low: string = Priority[Priority.Low];
  High: string = Priority[Priority.High];
  details: string[] = [];
  detailsAdded: boolean = false;
  description: Description = {} as Description;

  constructor(private route: ActivatedRoute, private router: Router, private ticketService: TicketService, private toastr: ToastrService, private userService: UserService) {
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
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  update(id: string, model: NgForm) {
    this.description.details = this.details;
    this.model.description = this.description;
    this.model.clientId = this.user.id;
    if(this.model.title === undefined) {
      this.model.title = null;
    }
    if(this.model.title === undefined) {
      this.model.title = null;
    }
    if(this.model.priority === undefined) {
      this.model.priority = null;
    }
    if(this.model.description.details.length === 0) {
      this.model.description = null;
    }
    console.log("description = ", this.model.description);
    this.ticketService.update(id, this.model).subscribe({
      next: response => {
        this.router.navigateByUrl('/tickets-list-client')
      },
      error: error => {
        this.toastr.error(error.error);
      },
      complete: () => {
        this.toastr.success("Ticket created!")
      }
    })
  }

    addDetail(detail: string) {
      if(this.details.find(d => d === detail)) {
        this.toastr.info("Detail already added!");
      } else {
        this.details.push(detail);
        this.detailsAdded = true;
        this.toastr.info("Detail added!");
      }
    }

    removeDetail(detail: string) {
      if(this.details.find(d => d === detail)) {
        this.details = this.filter(detail, this.details);
        this.toastr.info("Detail removed!");
      } else {
        this.toastr.info("Detail not found!")
      }
      if(this.details.length === 0) {
        this.detailsAdded = false;
        this.toastr.info("No details set for this ticket!");
      }
    }

    filter(detail: string, details: string[]) {
      let detailsFiltered: string[] = [];
      details.forEach(d => {
        if(d !== detail) {
          detailsFiltered.push(d);
        }
      })
      return detailsFiltered;
    }

}
