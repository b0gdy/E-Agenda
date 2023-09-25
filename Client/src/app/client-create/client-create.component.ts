import { Component, OnInit } from '@angular/core';
import {Role} from "../_models/role";
import {Employee} from "../_models/employee";
import {EmployeeService} from "../_services/employee.service";
import {RoleService} from "../_services/role.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {NgForm} from "@angular/forms";
import {Client} from "../_models/client";
import {ClientService} from "../_services/client.service";
import {CompanyService} from "../_services/company.service";
import {Company} from "../_models/company";

@Component({
  selector: 'app-client-create',
  templateUrl: './client-create.component.html',
  styleUrls: ['./client-create.component.css']
})
export class ClientCreateComponent implements OnInit {

  model:any = {}
  companies: Company[] = [];
  client: Client = {} as Client;

  constructor(private clientService: ClientService,
              private companyService: CompanyService,
              private router: Router,
              private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.getAllCompanies();
  }

  getAllCompanies(){
    this.companyService.getAll().subscribe({
      next: (response: Role[]) => {
        this.companies = response;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  create(model: NgForm) {
    this.clientService.create(this.model).subscribe({
      next: (response: Client) => {
        this.router.navigateByUrl('/clients-list')
      },
      error: err => {
        this.toastr.error(err.error);
      },
      complete: () => {
        this.toastr.success("Client created!")
      }
    })
  }

}
