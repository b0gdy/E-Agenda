import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Meeting} from "../_models/meeting";
import {UserService} from "./user.service";
import {catchError, map, throwError} from "rxjs";
import {Response} from "../_models/response";

@Injectable({
  providedIn: 'root'
})
export class MeetingService {

  baseUrl = 'http://localhost:8083/meetings';

  constructor(private http: HttpClient, private userService: UserService) { }

  mapResponseToMeeting(response: any) {
    let meeting: Meeting = {} as Meeting;
    meeting.id = response.id;
    meeting.date = new Date(response.date);
    meeting.date.setHours(0);
    meeting.date.setMinutes(0);
    meeting.date.setSeconds(0);
    meeting.date.setMilliseconds(0);
    meeting.start = new Date(meeting.date.getFullYear() + '-' + (meeting.date.getMonth() + 1) + '-' + meeting.date.getDate() + " " + response.start);
    meeting.end = new Date(meeting.date.getFullYear() + '-' + (meeting.date.getMonth() + 1) + '-' + meeting.date.getDate() + " " + response.end);
    meeting.createdAt = new Date(response.createdAt);
    meeting.updatedAt = new Date(response.updatedAt);
    meeting.participants = response.participantDtos;
    return meeting;
  }

  mapResponseToArrayOfMeetings(response: any) {
    let meetings: Meeting[] = [];
    response.forEach((r:any) => {
      let meeting: Meeting = {} as Meeting;
      meeting = this.mapResponseToMeeting(r);
      meetings.push(meeting);
    })
    return meetings;
  }

  create(model: any) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/create';
    let body = {
      date: model.startDate.getFullYear() + "-" + ('0' + (model.startDate.getMonth() + 1)).slice(-2) + "-" + ('0' + model.startDate.getDate()).slice(-2),
      start: ('0' + model.startDate.getHours()).slice(-2) + ":" + ('0' + model.startDate.getMinutes()).slice(-2),
      end: ('0' + model.endDate.getHours()).slice(-2) + ":" + ('0' + model.endDate.getMinutes()).slice(-2),
      participantIds: model.participantIds,
    }
    return this.http.post<Meeting>(url, body, options)
  }

  getAll() {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl;
    return this.http.get<Meeting[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfMeetings(response);
        }),
        catchError(err => {
          return throwError(err);
        })
      )
  }

  getById(id: string) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/' + id;
    return this.http.get<Meeting>(url, options).pipe(
      map((response:any) => {
        return this.mapResponseToMeeting(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByEmployee(employeeId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/get-by-employee/' + employeeId;
    return this.http.get<Meeting[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfMeetings(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByDateAndResponseAndEmployee(date: Date, response: Response, employeeId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('date', (date.getFullYear() + "-" + ('0' + (date.getMonth() + 1)).slice(-2) + "-" + ('0' + date.getDate()).slice(-2)))
      .set('response', Response[response])
      .set('employeeId', employeeId);
    let options = {headers: headers, params: params};
    let url = this.baseUrl + '/get-by-date-and-response-and-employee';
    return this.http.get<Meeting[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfMeetings(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  addResponse(id: string, response: Response, employeeId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('response', Response[response])
      .set('employeeId', employeeId);
    let options = {headers: headers};
    let url = this.baseUrl + '/add-response/' + id;
    return this.http.put<Meeting>(url, params, options).pipe(
      map((response:any) => {
        return this.mapResponseToMeeting(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  delete(id: string) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/' + id;
    return this.http.delete<string>(url, options)
  }

}
