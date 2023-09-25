import { Component, OnInit } from '@angular/core';
import {Company} from "../_models/company";
import {Client} from "../_models/client";
import {ClientService} from "../_services/client.service";
import {CompanyService} from "../_services/company.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Role} from "../_models/role";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-company-create',
  templateUrl: './company-create.component.html',
  styleUrls: ['./company-create.component.css']
})
export class CompanyCreateComponent implements OnInit {

  model:any = {}
  companies: Company[] = [];
  company: Company = {} as Company;

  constructor(private companyService: CompanyService,
              private router: Router,
              private toastr: ToastrService
  ) { }

  ngOnInit(): void {
  }

  create(model: NgForm) {
    this.companyService.create(this.model).subscribe({
      next: (response: Company) => {
        this.router.navigateByUrl('/companies-list')
      },
      error: err => {
        this.toastr.error(err.error);
      },
      complete: () => {
        this.toastr.success("Company created!")
      }
    })
  }

}
