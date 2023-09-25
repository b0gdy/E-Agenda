import { Component, OnInit } from '@angular/core';
import {Meeting} from "../_models/meeting";
import {MeetingService} from "../_services/meeting.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-meetings-list',
  templateUrl: './meetings-list.component.html',
  styleUrls: ['./meetings-list.component.css']
})
export class MeetingsListComponent implements OnInit {

  page: number = 1;
  size: number = 5;
  total: number = 0;
  meetings: Meeting[] = [];
  date: Date = new Date();

  constructor(private meetingService: MeetingService, private router: Router, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.date = new Date(this.date.getFullYear() + '-' + (this.date.getMonth() + 1) + '-' + (this.date.getDate()))
    this.date.setHours(0);
    this.date.setMinutes(0);
    this.date.setSeconds(0);
    this.date.setMilliseconds(0);
    this.getAll();
  }

  getAll() {
    this.meetingService.getAll().subscribe({
      next: (response: Meeting[]) => {
        this.meetings = response;
        this.total = this.meetings.length;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

}
