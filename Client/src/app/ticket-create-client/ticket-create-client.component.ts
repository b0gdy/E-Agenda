import {Component, OnInit} from '@angular/core';
import {Ticket} from "../_models/ticket";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {TicketService} from "../_services/ticket.service";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {User} from "../_models/user";
import {NgForm} from "@angular/forms";
import {Description} from "../_models/description";
import {Priority} from "../_models/priority";

@Component({
  selector: 'app-ticket-create-client',
  templateUrl: './ticket-create-client.component.html',
  styleUrls: ['./ticket-create-client.component.css']
})
export class TicketCreateClientComponent implements OnInit {

  model:any = {}
  model_aux:any = {}
  ticket: Ticket = {} as Ticket;
  user: User = {} as User;
  details: string[] = [];
  detailsAdded: boolean = false;
  description: Description = {} as Description;
  Low: string = Priority[Priority.Low];
  High: string = Priority[Priority.High];

  constructor(private ticketService: TicketService, private router: Router, private toastr: ToastrService, private userService: UserService) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if(user) {
          this.user = user;
        }
      }
    });
  }

  ngOnInit(): void {
    this.detailsAdded = false;
  }

  create(model: NgForm) {
    this.description.details = this.details;
    this.model.description = this.description;
    this.model.clientId = this.user.id;
    this.ticketService.create(this.model).subscribe({
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
