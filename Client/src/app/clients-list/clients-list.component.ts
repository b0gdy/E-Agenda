import { Component, OnInit } from '@angular/core';
import {Employee} from "../_models/employee";
import {User} from "../_models/user";
import {EmployeeService} from "../_services/employee.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {Client} from "../_models/client";
import {ClientService} from "../_services/client.service";

@Component({
  selector: 'app-clients-list',
  templateUrl: './clients-list.component.html',
  styleUrls: ['./clients-list.component.css']
})
export class ClientsListComponent implements OnInit {

  clients: Client[] = [];
  page: number = 1;
  size: number = 5;
  total: number = 0;
  disableShown: boolean = false;
  user: User = {} as User;
  fetched: boolean = false;

  constructor(private clientService: ClientService, private router: Router, private toastr: ToastrService, private userService: UserService) {
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
    this.clientService.getAll().subscribe({
      next: (response: Client[]) => {
        this.clients = response;
        this.total = this.clients.length;
        this.fetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  getAllEnabled() {
    this.clientService.getAllEnabled().subscribe({
      next: (response: Client[]) => {
        this.clients = response;
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
