import { Component, OnInit } from '@angular/core';
import {EmployeeService} from "../_services/employee.service";
import {ActivatedRoute, Params} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Employee} from "../_models/employee";

@Component({
  selector: 'app-account-activated',
  templateUrl: './account-activated.component.html',
  styleUrls: ['./account-activated.component.css']
})
export class AccountActivatedComponent implements OnInit {

  username: string = {} as string;
  activated: boolean = false;


  constructor(private route: ActivatedRoute,  private employeeService: EmployeeService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.route.params.subscribe({
      next: (params: Params) => {
        this.username= params['username'];
        this.activateAccount(this.username);
      }
    })
  }

  activateAccount(username: string) {
    console.log("username = ", username);
    this.employeeService.activateAccount(username).subscribe({
      next: (response: Employee) => {
        console.log("response = ", response);
        this.toastr.success("Account activated!");
        this.activated = true;
      },
      error: error => {
        console.log("error = ", error);
        this.toastr.error(error.error);
      }
    })
  }

}
