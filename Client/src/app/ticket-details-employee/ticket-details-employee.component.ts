import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {MeetingService} from "../_services/meeting.service";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {User} from "../_models/user";
import {Ticket} from "../_models/ticket";
import {Employee} from "../_models/employee";
import {Meeting} from "../_models/meeting";
import {TicketService} from "../_services/ticket.service";
import {tick} from "@angular/core/testing";
import {Status} from "../_models/status";

@Component({
  selector: 'app-ticket-details-employee',
  templateUrl: './ticket-details-employee.component.html',
  styleUrls: ['./ticket-details-employee.component.css']
})
export class TicketDetailsEmployeeComponent implements OnInit {

  user: User = {} as User;
  ticketId: string = {} as string;
  ticket: Ticket = {} as Ticket;
  employee: Employee = {} as Employee;
  ticketFetched: boolean = false;
  Complete: string = Status[Status.Complete];
  Working: string = Status[Status.Working];


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
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  updateEmployee(employeeId: number) {
    this.ticketFetched = false;
    this.ticketService.updateEmployee(this.ticket.id, employeeId).subscribe({
      next: (response: Ticket) => {
        this.ticket = response;
        this.ticketFetched = true;
      },
      error: err => {
        this.ticketFetched = true;
        this.toastr.error(err.error);
      },
      complete: () => {
        if (employeeId !== 0) {
          this.toastr.success("Ticket assigned to employee " + this.ticket.employee.firstName + " " + this.ticket.employee.lastName + "!");
        } else {
          this.toastr.success("Ticket unassigned!");
        }
      }
    })
  }

  updateStatus(status: string) {
    this.ticketFetched = false;
    this.ticketService.updateStatus(this.ticket.id, status).subscribe({
      next: (response: Ticket) => {
        this.ticket = response;
        this.ticketFetched = true;
      },
      error: err => {
        this.ticketFetched = true;
        this.toastr.error(err.error);
      },
      complete: () => {
        if (status === this.Complete) {
          this.toastr.success("Ticket completed!");
        } else {
          this.toastr.success("Ticket still in working!");
        }
      }
    })
  }

}
