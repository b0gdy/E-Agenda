import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {UserService} from "./user.service";
import {Meeting} from "../_models/meeting";
import {catchError, map, throwError} from "rxjs";
import {BusyTime} from "../_models/busyTimes";
import {Response} from "../_models/response";

@Injectable({
  providedIn: 'root'
})
export class BusyTimeService {

  baseUrl = 'http://localhost:8083/busy-times';

  constructor(private http: HttpClient, private userService: UserService) { }

  mapResponseToBusyTime(response: any) {
    let busyTime: BusyTime = {} as BusyTime;
    busyTime.id = response.id;
    busyTime.date = new Date(response.date);
    busyTime.date.setHours(0);
    busyTime.date.setMinutes(0);
    busyTime.date.setSeconds(0);
    busyTime.date.setMilliseconds(0);
    busyTime.start = new Date(busyTime.date.getFullYear() + '-' + (busyTime.date.getMonth() + 1) + '-' + busyTime.date.getDate() + " " + response.start);
    busyTime.end = new Date(busyTime.date.getFullYear() + '-' + (busyTime.date.getMonth() + 1) + '-' + busyTime.date.getDate() + " " + response.end);
    busyTime.employee = response.employee;
    busyTime.description = response.description;
    return busyTime;
  }

  mapResponseToArrayOfBusyTimes(response: any) {
    let busyTimes: BusyTime[] = [];
    response.forEach((r:any) => {
      let busyTime: BusyTime = {} as BusyTime;
      busyTime = this.mapResponseToBusyTime(r);
      busyTimes.push(busyTime);
    })
    return busyTimes;
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
      employeeId: model.employeeId,
      description: model.description
    }
    return this.http.post<BusyTime>(url, body, options)
  }

  getAll() {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl;
    return this.http.get<BusyTime[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfBusyTimes(response);
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
    return this.http.get<BusyTime>(url, options).pipe(
      map((response:any) => {
        return this.mapResponseToBusyTime(response);
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
    let url = this.baseUrl + '/get-future-and-recent-past-busy-times-by-employee/' + employeeId;
    return this.http.get<BusyTime[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfBusyTimes(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByDateAndEmployee(date: Date, employeeId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('date', (date.getFullYear() + "-" + ('0' + (date.getMonth() + 1)).slice(-2) + "-" + ('0' + date.getDate()).slice(-2)))
      .set('employeeId', employeeId);
    let options = {headers: headers, params: params};
    let url = this.baseUrl + '/get-by-date-and-employee';
    return this.http.get<Meeting[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfBusyTimes(response);
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
