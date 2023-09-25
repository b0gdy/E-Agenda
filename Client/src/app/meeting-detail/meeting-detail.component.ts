import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Meeting} from "../_models/meeting";
import {MeetingService} from "../_services/meeting.service";
import {Participant} from "../_models/participant";
import {Response} from "../_models/response";
import {Employee} from "../_models/employee";

@Component({
  selector: 'app-meeting-detail',
  templateUrl: './meeting-detail.component.html',
  styleUrls: ['./meeting-detail.component.css']
})
export class MeetingDetailComponent implements OnInit {

  meetingId: string = {} as string;
  meeting: Meeting = {} as Meeting;
  participants: Participant[] = [];
  participant: Participant = {} as Participant;
  response: Response = {} as Response;
  employee: Employee = {} as Employee
  YES: string = Response[Response.YES];
  NO: string = Response[Response.NO];
  meetingFetched: boolean = false;

  constructor(private route: ActivatedRoute, private router: Router, private meetingService: MeetingService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.meetingFetched = false;
    this.route.params.subscribe({
      next: (params: Params) => {
        this.meetingId= params['id'];
        this.getById(this.meetingId);
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  getById(id: string) {
    this.meetingService.getById(id).subscribe({
      next: (response: Meeting) => {
        this.meeting = response;
        this.meetingFetched = true;
      },
      error: err => {
        this.toastr.error(err.error);
      }
    })
  }

  delete(id: string) {
    this.meetingService.delete(id).subscribe({
      next: (response: string) => {
        // this.toastr.success(response);
        this.router.navigateByUrl('/meetings-list');
      },
      error: err => {
        // this.toastr.error(err.error);
        this.router.navigateByUrl('/meetings-list');
      }
    })
  }

}
