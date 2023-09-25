import { Component, OnInit } from '@angular/core';
import {Employee} from "../_models/employee";
import {EmployeeService} from "../_services/employee.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {User} from "../_models/user";

@Component({
  selector: 'app-employees-list',
  templateUrl: './employees-list.component.html',
  styleUrls: ['./employees-list.component.css']
})
export class EmployeesListComponent implements OnInit {

  employees: Employee[] = [];
  page: number = 1;
  size: number = 5;
  total: number = 0;
  disableShown: boolean = false;
  user: User = {} as User;
  fetched: boolean = false;

  constructor(private employeeService: EmployeeService, private router: Router, private toastr: ToastrService, private userService: UserService) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if(user) {
          this.user = user;
        }
      }
    });
  }

  ngOnInit(): void {
    this.fetched = false;
    this.disableShown = false;
    this.getAllEnabled();
  }

  getAll() {
    this.employeeService.getAll().subscribe({
      next: (response: Employee[]) => {
        this.employees = response;
        this.total = this.employees.length;
        this.fetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  getAllEnabled() {
    this.employeeService.getAllEnabled().subscribe({
      next: (response: Employee[]) => {
        this.employees = response;
        this.fetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  showDisabled() {
    if(this.disableShown) {
      this.fetched = false;
      this.disableShown = false;
      this.getAllEnabled();
    } else if(!this.disableShown) {
      this.fetched = false;
      this.disableShown = true;
      this.getAll();
    }
  }

}
