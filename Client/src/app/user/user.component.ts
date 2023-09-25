import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { User } from '../_models/user';
import { take } from 'rxjs';
import {EmployeeService} from "../_services/employee.service";
import {ClientService} from "../_services/client.service";
import {Employee} from "../_models/employee";
import {Client} from "../_models/client";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  user: User = {} as User;
  employee: Employee = {} as Employee;
  client: Client = {} as Client;
  loaded: boolean = false;

  constructor(private userService: UserService, private employeeService: EmployeeService, private clientService: ClientService, private toastr: ToastrService) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if (user) {
          this.user = user;
        }
      }
    })
  }

  ngOnInit(): void {
    this.loaded = false;
    if ((this.user.role === "employee") || (this.user.role === "admin")) {
      this.getEmployeeById(this.user.id);
    } else if (this.user.role === "client") {
      this.getClientById(this.user.id);
    }
  }

  getEmployeeById(id: number) {
    this.employeeService.getById(id).subscribe({
      next: (response: Employee) => {
        this.employee = response;
        this.loaded = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  getClientById(id: number) {
    this.clientService.getById(id).subscribe({
      next: (response: Client) => {
        this.client = response;
        this.loaded = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }


  protected readonly localStorage = localStorage;

}
