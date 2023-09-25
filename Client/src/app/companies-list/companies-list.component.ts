import { Component, OnInit } from '@angular/core';
import {Client} from "../_models/client";
import {User} from "../_models/user";
import {ClientService} from "../_services/client.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {Company} from "../_models/company";
import {CompanyService} from "../_services/company.service";

@Component({
  selector: 'app-companies-list',
  templateUrl: './companies-list.component.html',
  styleUrls: ['./companies-list.component.css']
})
export class CompaniesListComponent implements OnInit {

  companies: Company[] = [];
  page: number = 1;
  size: number = 5;
  total: number = 0;
  user: User = {} as User;
  fetched: boolean = false;

  constructor(private companyService: CompanyService, private router: Router, private toastr: ToastrService, private userService: UserService) {
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
    this.getAll();
  }

  getAll() {
    this.companyService.getAll().subscribe({
      next: (response: Company[]) => {
        this.companies = response;
        this.total = this.companies.length;
        this.fetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }
}
