import { Component, OnInit } from '@angular/core';
import {EmployeeService} from "../_services/employee.service";
import {ToastrService} from "ngx-toastr";
import {NgForm, NgModel} from "@angular/forms";
import {RoleService} from "../_services/role.service";
import {Role} from "../_models/role";
import {Employee} from "../_models/employee";
import {add} from "ngx-bootstrap/chronos";
import {Router} from "@angular/router";

@Component({
  selector: 'app-employee-create',
  templateUrl: './employee-create.component.html',
  styleUrls: ['./employee-create.component.css']
})
export class EmployeeCreateComponent implements OnInit {

  model:any = {}
  roles: Role[] = [];
  roleNames: string[] = [];
  employee: Employee = {} as Employee;

  constructor(private employeeService: EmployeeService,
              private roleService: RoleService,
              private router: Router,
              private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.getAllRoles();
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

  create(model: NgForm) {
    this.employeeService.create(this.model).subscribe({
      next: response => {
        this.router.navigateByUrl('/employees-list')
      },
      error: error => {
        this.toastr.error(error.error);
      },
      complete: () => {
        this.toastr.success("Employee created!")
      }
    })
  }

}
