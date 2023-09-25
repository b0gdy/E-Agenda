import { Component, OnInit } from '@angular/core';
import {User} from "../_models/user";
import {Ticket} from "../_models/ticket";
import {Employee} from "../_models/employee";
import {Status} from "../_models/status";
import {ActivatedRoute, Params} from "@angular/router";
import {TicketService} from "../_services/ticket.service";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {Role} from "../_models/role";
import {EmployeeService} from "../_services/employee.service";

@Component({
  selector: 'app-ticket-details',
  templateUrl: './ticket-details.component.html',
  styleUrls: ['./ticket-details.component.css']
})
export class TicketDetailsComponent implements OnInit {

  user: User = {} as User;
  ticketId: string = {} as string;
  ticket: Ticket = {} as Ticket;
  employee: Employee = {} as Employee;
  employees: Employee[] = [];
  ticketFetched: boolean = false;
  employeesFetched: boolean = false;


  constructor(private route: ActivatedRoute, private ticketService: TicketService, private toastr: ToastrService, private employeeService: EmployeeService, private userService: UserService) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if(user) {
          this.user = user;
        }
      }
    });
  }

  ngOnInit(): void {
    this.ticketFetched = false;
    this.employeesFetched = false;
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
        this.employeesFetched = false;
        this.getAllEmployees();
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  updateEnabled(enabled: boolean) {
    this.ticketFetched = false;
    this.ticketService.updateEnabled(this.ticket.id, enabled).subscribe({
      next: (response: any) => {
        this.ticket = response;
        this.ticketFetched = true;
        if(this.ticket.enabled) {
          this.toastr.success("Ticket enabled!")
        } else {
          this.toastr.success("Ticket disabled!")
        }
      },
      error: err => {
        this.toastr.error(err.error);
        this.ticketFetched = true;
        console.log("error = ", err.error)
      }
    })
  }

  getAllEmployees(){
    this.employeeService.getAllEnabled().subscribe({
      next: (response: Employee[]) => {
        this.employees = response;
        this.employeesFetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
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

}
