import { Component, OnInit } from '@angular/core';
import {Role} from "../_models/role";
import {Employee} from "../_models/employee";
import {EmployeeService} from "../_services/employee.service";
import {RoleService} from "../_services/role.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-employee-update',
  templateUrl: './employee-update.component.html',
  styleUrls: ['./employee-update.component.css']
})
export class EmployeeUpdateComponent implements OnInit {

  model:any = {}
  roles: Role[] = [];
  roleNames: string[] = [];
  employee: Employee = {} as Employee;
  employeeId: number = {} as number;

  constructor(private employeeService: EmployeeService,
              private roleService: RoleService,
              private router: Router,
              private toastr: ToastrService,
              private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe({
      next: (params: Params) => {
        this.employeeId= params['id'];
        this.getById(this.employeeId);
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
    this.getAllRoles();
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

  getAllRoles(){
    this.roleService.getAll().subscribe({
      next: (response: Role[]) => {
        this.roles = response;
        this.roles.forEach(role => {
          this.roleNames.push(role.name)
        });
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  update(id: number, model: NgForm) {
    console.log("firstName = ", this.model.firstName);
    console.log("lastName = ", this.model.lastName);
    console.log("position = ", this.model.position);
    console.log("salary = ", this.model.salary);
    console.log("role = ", this.model.role);
    console.log("password = ", this.model.password);
    if(this.model.firstName === undefined) {
      this.model.firstName = this.employee.firstName;
    }
    if(this.model.lastName === undefined) {
      this.model.lastName = this.employee.lastName;
    }
    if(this.model.position === undefined) {
      this.model.position = this.employee.position;
    }
    if(this.model.salary === undefined) {
      this.model.salary = this.employee.salary;
    }
    if(this.model.role === undefined) {
      this.model.role = this.employee.role;
    }
    if(this.model.password === undefined) {
      this.model.password = null;
    }
    this.employeeService.update(id, this.model).subscribe({
      next: response => {
        this.router.navigateByUrl('/employees-list')
        console.log("response = ", response);
      },
      error: err => {
        this.toastr.error(err.error);
        console.log("err = ", err);
        console.log("error = ", err.error);
      },
      complete: () => {
        this.toastr.success("Employee updated!")
      }
    })
  }

}
