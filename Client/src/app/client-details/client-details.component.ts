import { Component, OnInit } from '@angular/core';
import {Employee} from "../_models/employee";
import {User} from "../_models/user";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {EmployeeService} from "../_services/employee.service";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {Client} from "../_models/client";
import {ClientService} from "../_services/client.service";

@Component({
  selector: 'app-client-details',
  templateUrl: './client-details.component.html',
  styleUrls: ['./client-details.component.css']
})
export class ClientDetailsComponent implements OnInit {

  clientId: number = {} as number;
  client: Client = {} as Client;
  user: User = {} as User;
  fetched: boolean = false;

  constructor(private route: ActivatedRoute, private router: Router, private clientService: ClientService, private toastr: ToastrService, private userService: UserService) {
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
    this.route.params.subscribe({
      next: (params: Params) => {
        this.clientId= params['id'];
        this.getById(this.clientId);
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  getById(id: number) {
    this.clientService.getById(id).subscribe({
      next: (response: Client) => {
        this.client = response;
        this.fetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
        this.fetched = false;
      }
    })
  }

  updateEnabled(enabled: boolean) {
    this.fetched = false;
    this.clientService.updateEnabled(this.client.id, enabled).subscribe({
      next: (response: Client) => {
        this.client = response;
        this.fetched = true
      },
      error: error => {
        this.toastr.error(error.error);
        this.fetched = true;
      },
      complete: () => {
        if (enabled) {
          this.toastr.success("Client enabled!");
        } else {
          this.toastr.success("Client disabled!");
        }
      }
    })
  }

}
