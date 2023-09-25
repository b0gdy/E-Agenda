import { Component, OnInit } from '@angular/core';
import {Company} from "../_models/company";
import {Client} from "../_models/client";
import {ClientService} from "../_services/client.service";
import {CompanyService} from "../_services/company.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Role} from "../_models/role";
import {NgForm} from "@angular/forms";
import {User} from "../_models/user";
import {UserService} from "../_services/user.service";
import {UserFull} from "../_models/UserFull";

@Component({
  selector: 'app-your-account',
  templateUrl: './your-account.component.html',
  styleUrls: ['./your-account.component.css']
})
export class YourAccountComponent implements OnInit {

  model:any = {}
  user: UserFull = {} as UserFull;
  userId: number = {} as number;
  userFetched: boolean = false;

  constructor(private userService: UserService,
              private router: Router,
              private toastr: ToastrService,
              private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.userFetched = false;
    this.route.params.subscribe({
      next: (params: Params) => {
        this.userId= params['id'];
        this.getById(this.userId);
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  getById(id: number) {
    this.userService.getById(id).subscribe({
      next: (response: UserFull) => {
        this.user = response;
        this.userFetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  update(id: number, model: NgForm) {
    console.log("firstName = ", this.model.firstName);
    console.log("lastName = ", this.model.lastName);
    console.log("password = ", this.model.password);
    if(this.model.firstName === undefined) {
      this.model.firstName = this.user.firstName;
    }
    if(this.model.lastName === undefined) {
      this.model.lastName = this.user.lastName;
    }
    if(this.model.password === undefined) {
      this.model.password = null;
    }
    this.userService.update(id, this.model).subscribe({
      next: response => {
        this.router.navigateByUrl('/users-list')
        console.log("response = ", response);
      },
      error: err => {
        this.toastr.error(err.error);
        console.log("err = ", err);
        console.log("error = ", err.error);
      },
      complete: () => {
        this.toastr.success("User updated!")
      }
    })
  }

}
