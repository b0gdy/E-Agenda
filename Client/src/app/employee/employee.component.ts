import { Component, OnInit } from '@angular/core';
import {Employee} from "../_models/employee";
import {EmployeeService} from "../_services/employee.service";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {User} from "../_models/user";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {

  user: User = {} as User;
  employee: Employee = {} as Employee;

  constructor(private employeeService: EmployeeService, private userService: UserService, private toastr: ToastrService){
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if(user) {
          this.user = user;
        }
      }
    });
  }

  ngOnInit(): void {
    this.getById(this.user.id);
  }

  getById(id: number) {
    this.employeeService.getById(id).subscribe({
      next: (response: Employee) => {
        this.employee = response;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

}
