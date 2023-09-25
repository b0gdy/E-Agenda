import { Component, OnInit } from '@angular/core';
import {Role} from "../_models/role";
import {Employee} from "../_models/employee";
import {EmployeeService} from "../_services/employee.service";
import {RoleService} from "../_services/role.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {NgForm} from "@angular/forms";
import {Client} from "../_models/client";
import {ClientService} from "../_services/client.service";
import {Company} from "../_models/company";
import {CompanyService} from "../_services/company.service";

@Component({
  selector: 'app-client-update',
  templateUrl: './client-update.component.html',
  styleUrls: ['./client-update.component.css']
})
export class ClientUpdateComponent implements OnInit {

  model:any = {}
  companies: Company[] = [];
  client: Client = {} as Client;
  clientId: number = {} as number;
  clientFetched: boolean = false;
  companiesFetched: boolean = false;

  constructor(private clientService: ClientService,
              private companyService: CompanyService,
              private router: Router,
              private toastr: ToastrService,
              private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.clientFetched = false;
    this.companiesFetched = false;
    this.route.params.subscribe({
      next: (params: Params) => {
        this.clientId= params['id'];
        this.getById(this.clientId);
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
    this.getAllCompanies();
  }

  getById(id: number) {
    this.clientService.getById(id).subscribe({
      next: (response: Client) => {
        this.client = response;
        this.clientFetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  getAllCompanies(){
    this.companyService.getAll().subscribe({
      next: (response: Role[]) => {
        this.companies = response;
        this.companiesFetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  update(id: number, model: NgForm) {
    console.log("firstName = ", this.model.firstName);
    console.log("lastName = ", this.model.lastName);
    console.log("companyId = ", this.model.companyId);
    console.log("password = ", this.model.password);
    if(this.model.firstName === undefined) {
      this.model.firstName = this.client.firstName;
    }
    if(this.model.lastName === undefined) {
      this.model.lastName = this.client.lastName;
    }
    if(this.model.companyId === undefined) {
      this.model.companyId = this.client.company.id;
    }
    if(this.model.password === undefined) {
      this.model.password = null;
    }
    this.clientService.update(id, this.model).subscribe({
      next: response => {
        this.router.navigateByUrl('/clients-list')
        console.log("response = ", response);
      },
      error: err => {
        this.toastr.error(err.error);
        console.log("err = ", err);
        console.log("error = ", err.error);
      },
      complete: () => {
        this.toastr.success("Client updated!")
      }
    })
  }

}
