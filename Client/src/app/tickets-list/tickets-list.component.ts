import { Component, OnInit } from '@angular/core';
import {Meeting} from "../_models/meeting";
import {MeetingService} from "../_services/meeting.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {TicketService} from "../_services/ticket.service";
import {Ticket} from "../_models/ticket";
import {Status} from "../_models/status";
import {Priority} from "../_models/priority";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-tickets-list',
  templateUrl: './tickets-list.component.html',
  styleUrls: ['./tickets-list.component.css']
})
export class TicketsListComponent implements OnInit {

  page: number = 1;
  size: number = 5;
  total: number = 0;
  tickets: Ticket[] = [];
  date: Date = new Date();
  Complete = Status[Status.Complete];
  Working = Status[Status.Working];
  High = Priority[Priority.High];
  Low = Priority[Priority.Low];
  disableShown: boolean = false;
  model:any = {}
  fetched: boolean = false;



  constructor(private ticketService: TicketService, private router: Router, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.fetched = false;
    this.disableShown = false;
    this.date = new Date(this.date.getFullYear() + '-' + (this.date.getMonth() + 1) + '-' + (this.date.getDate()))
    this.date.setHours(0);
    this.date.setMinutes(0);
    this.date.setSeconds(0);
    this.date.setMilliseconds(0);
    this.getAllEnabled();
  }

  getAll() {
    this.ticketService.getAll().subscribe({
      next: (response: Ticket[]) => {
        this.tickets = response;
        this.total = this.tickets.length;
        this.fetched = true;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  getAllEnabled() {
    this.ticketService.getAllEnabled().subscribe({
      next: (response: Ticket[]) => {
        this.tickets = response;
        this.total = this.tickets.length;
        this.fetched = true;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  getByTitle(title: string) {
    this.ticketService.getByTitle(title).subscribe({
      next: (response: Ticket[]) => {
        this.tickets = response;
        this.total = this.tickets.length;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  getByTitleAndEnabled(title: string) {
    this.ticketService.getByTitleAndEnabled(title).subscribe({
      next: (response: Ticket[]) => {
        this.tickets = response;
        this.total = this.tickets.length;
      },
      error: err => {
        this.toastr.error(err.error);
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

  searchByTitle(title: string) {
    if(title === undefined || title === "") {
      title = "no_title";
    }
    if(this.disableShown){
      this.getByTitle(title);
    } else {
      this.getByTitleAndEnabled(title);
    }
  }

}
