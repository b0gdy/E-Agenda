import { Component, OnInit } from '@angular/core';
import {Company} from "../_models/company";
import {Client} from "../_models/client";
import {ClientService} from "../_services/client.service";
import {CompanyService} from "../_services/company.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Role} from "../_models/role";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-company-update',
  templateUrl: './company-update.component.html',
  styleUrls: ['./company-update.component.css']
})
export class CompanyUpdateComponent implements OnInit {

  model:any = {}
  company: Company = {} as Company;
  companyId: number = {} as number;
  companyFetched: boolean = false;

  constructor(private companyService: CompanyService,
              private router: Router,
              private toastr: ToastrService,
              private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.companyFetched = false;
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
        this.companyFetched = true;
      },
      error: error => {
        this.toastr.error(error.error);
      }
    })
  }

  update(id: number, model: NgForm) {
    if(this.model.name === undefined) {
      this.model.name = this.company.name;
    }
    this.companyService.update(id, this.model).subscribe({
      next: response => {
        this.router.navigateByUrl('/companies-list')
      },
      error: err => {
        this.toastr.error(err.error);
      },
      complete: () => {
        this.toastr.success("Company updated!")
      }
    })
  }

}
