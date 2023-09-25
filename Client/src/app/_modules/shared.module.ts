import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ToastrModule } from 'ngx-toastr';
import {BsDatepickerModule} from "ngx-bootstrap/datepicker";
import {NgxPaginationModule} from "ngx-pagination";

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    BsDropdownModule.forRoot(),
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-right'
    }),
    NgxPaginationModule
  ],
  exports: [
    BsDropdownModule,
    ToastrModule,
    BsDatepickerModule,
    NgxPaginationModule
  ]
})
export class SharedModule { }
