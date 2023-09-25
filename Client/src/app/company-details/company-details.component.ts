import { Component, OnInit } from '@angular/core';
import {Client} from "../_models/client";
import {User} from "../_models/user";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ClientService} from "../_services/client.service";
import {ToastrService} from "ngx-toastr";
import {UserService} from "../_services/user.service";
import {take} from "rxjs";
import {Company} from "../_models/company";
import {CompanyService} from "../_services/company.service";

@Component({
  selector: 'app-company-details',
  templateUrl: './company-details.component.html',
  styleUrls: ['./company-details.component.css']
})
export class CompanyDetailsComponent implements OnInit {

  companyId: number = {} as number;
  company: Company = {} as Company;
  user: User = {} as User;
  fetched: boolean = false;
  clients: Client[] = [];
  clientsFetched: boolean = false;

  constructor(private route: ActivatedRoute, private router: Router, private companyService: CompanyService, private clientService: ClientService, private toastr: ToastrService, private userService: UserService) {
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
    this.clientsFetched = false;
    this.route.params.subscribe({
      next: (params: Params) => {
        this.companyId= params['id'];
        this.getById(this.companyId);
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  getById(id: number) {
    this.companyService.getById(id).subscribe({
      next: (response: Company) => {
        this.company = response;
        this.fetched = true;
        this.clientsFetched = false;
        this.getClientsByCompany(this.company.id);
      },
      error: error => {
        this.toastr.error(error.error);
        this.fetched = false;
      }
    })
  }

  getClientsByCompany(id:number) {
    this.clientService.getByCompany(id).subscribe({
      next: (response: Client[]) => {
        this.clients = response;
        this.clientsFetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
        this.clientsFetched = false;
      }
    })
  }


}
