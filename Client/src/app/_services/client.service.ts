import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {UserService} from "./user.service";
import {Client} from "../_models/client";
import {Ticket} from "../_models/ticket";
import {Company} from "../_models/company";
import {catchError, map, throwError} from "rxjs";
import {Employee} from "../_models/employee";
import {Meeting} from "../_models/meeting";

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  baseUrl = 'http://localhost:8080/clients';
  // baseUrl = 'http://authentication:8080/clients';

  constructor(private http: HttpClient, private userService: UserService) { }

  mapResponseToClient(response: any) {
    let client: Client = {} as Client;
    client.id = response.id;
    client.userName = response.userName;
    client.enabled = response.enabled;
    client.role = response.role;
    client.firstName = response.firstName;
    client.lastName = response.lastName;
    client.company = response.companyDto;
    return client;
  }

  mapResponseToArrayOfClients(response: any) {
    let clients: Client[] = [];
    response.forEach((r:any) => {
      let client: Client = {} as Client;
      client = this.mapResponseToClient(r);
      clients.push(client);
    })
    return clients;
  }

  create(model: any) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/create';
    let body = {
      userName: model.userName,
      password: model.password,
      // role: "client",
      firstName: model.firstName,
      lastName: model.lastName,
      companyId: model.companyId,
    }
    return this.http.post<Client>(url, body, options).pipe(
      map((response:any) => {
        return this.mapResponseToClient(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getAll() {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl;
    return this.http.get<Client[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfClients(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getAllEnabled() {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/enabled';
    return this.http.get<Client[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfClients(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getById(id: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/' + id;
    return this.http.get<Client>(url, options).pipe(
      map((response:any) => {
        return this.mapResponseToClient(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByCompany(id: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/get-by-company/' + id;
    return this.http.get<Client[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfClients(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  update(id: number, model: any) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('firstName', model.firstName)
      .set('lastName', model.lastName)
      .set('companyId', model.companyId)
      .set('password', model.password)
    console.log("firstName = ", params.get("firstName"));
    console.log("lastName = ", params.get("lastName"));
    console.log("companyId = ", params.get("companyId"));
    console.log("password = ", params.get("password"));
    let options = {headers: headers};
    let url = this.baseUrl + '/' + id
    return this.http.put<Client>(url, params, options).pipe(
      map((response:any) => {
        return this.mapResponseToClient(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  updateEnabled(id: number, enabled: boolean) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('enabled', enabled)
    let options = {headers: headers};
    let url = this.baseUrl + '/update-enabled/' + id
    return this.http.put<Client>(url, params, options).pipe(
      map((response:any) => {
        return this.mapResponseToClient(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

}
