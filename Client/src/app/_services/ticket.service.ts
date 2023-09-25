import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Ticket} from "../_models/ticket";
import {Meeting} from "../_models/meeting";
import {catchError, map, throwError} from "rxjs";
import {Response} from "../_models/response";
import {Priority} from "../_models/priority";
import {Status} from "../_models/status";

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  baseUrl = 'http://localhost:8084/tickets';
  // baseUrl = 'http://authentication:8084/tickets';

  constructor(private http: HttpClient) { }

  mapResponseToTicket(response: any) {
    let ticket: Ticket = {} as Ticket;
    ticket.id = response.id;
    ticket.title = response.title;
    ticket.description = response.description;
    ticket.createdAt = new Date(response.createdAt);
    ticket.updatedAt = new Date(response.updatedAt);
    ticket.employee = response.employee;
    ticket.client = response.client;
    ticket.status = response.status;
    ticket.priority = response.priority;
    ticket.enabled = response.enabled;
    return ticket;
  }

  mapResponseToArrayOfTickets(response: any) {
    let tickets: Ticket[] = [];
    response.forEach((r:any) => {
      let ticket: Ticket = {} as Ticket;
      ticket = this.mapResponseToTicket(r);
      tickets.push(ticket);
    })
    return tickets;
  }

  create(model: any) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/create';
    let body = {
      title: model.title,
      clientId: model.clientId,
      priority: Priority[model.priority],
      description: model.description,
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
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
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
    let params = new HttpParams()
      .set('enabled', true)
    let options = {headers: headers, params: params};
    let url = this.baseUrl + "/get-by-enabled";
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
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
    return this.http.get<Ticket>(url, options).pipe(
      map((response:any) => {
        return this.mapResponseToTicket(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByTitle(title: string) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/get-by-title/' + title;
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByTitleAndEnabled(title: string) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('enabled', true)
    let options = {headers: headers, params: params};
    let url = this.baseUrl + '/get-by-title-and-enabled/' + title;
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByClient(clientId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/get-by-client/' + clientId;
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByTitleAndClient(title: string, clientId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('title', title)
      .set('clientId', clientId);
    let options = {headers: headers, params: params};
    let url = this.baseUrl + '/get-by-title-and-client';
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByCompany(companyId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/get-by-company/' + companyId;
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByTitleAndCompany(title: string, companyId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('title', title)
      .set('companyId', companyId);
    let options = {headers: headers, params: params};
    let url = this.baseUrl + '/get-by-title-and-company';
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByCompanyAndEnabled(companyId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('enabled', true)
    let options = {headers: headers, params: params};
    let url = this.baseUrl + '/get-by-company-and-enabled/' + companyId;
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByTitleAndCompanyAndEnabled(title: string, companyId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('title', title)
      .set('companyId', companyId)
      .set('enabled', true);
    let options = {headers: headers, params: params};
    let url = this.baseUrl + '/get-by-title-and-company-and-enabled';
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
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
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  getByEmployeeAndEnabled(employeeId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('enabled', true)
    let options = {headers: headers, params: params};
    let url = this.baseUrl + '/get-by-employee-and-enabled/' + employeeId;
    return this.http.get<Ticket[]>(url, options).pipe(
      map((response:any[]) => {
        return this.mapResponseToArrayOfTickets(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  updateEmployee(id: string, employeeId: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('employeeId', employeeId)
    let options = {headers: headers};
    let url = this.baseUrl + '/update-employee/' + id;
    return this.http.put<Ticket>(url, params, options).pipe(
      map((response:any) => {
        return this.mapResponseToTicket(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  updateStatus(id: string, status: string) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('status', status)
    let options = {headers: headers};
    let url = this.baseUrl + '/update-status/' + id;
    return this.http.put<Ticket>(url, params, options).pipe(
      map((response:any) => {
        return this.mapResponseToTicket(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  update(id: string, model: any) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let body = {
      title: model.title,
      // employeeId: model.employee.id,
      // clientId: model.client.id,
      priority: model.priority,
      description: model.description,
    }
    let options = {headers: headers};
    let url = this.baseUrl + '/' + id;
    return this.http.put<Ticket>(url, body, options).pipe(
      map((response:any) => {
        return this.mapResponseToTicket(response);
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  updateEnabled(id: string, enabled: boolean) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('enabled', enabled);
    console.log(params.get('enabled'));
    let options = {headers: headers};
    let url = this.baseUrl + '/update-enabled/' + id;
    return this.http.put<Ticket>(url, params, options).pipe(
      map((response:any) => {
        return this.mapResponseToTicket(response);
      }),
      catchError(err => {
        console.log("err = ", err)
        return throwError(err);
      })
    )
  }

  disable(id: string) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('enabled', false);
    console.log(params.get('enabled'));
    let options = {headers: headers};
    let url = this.baseUrl + '/update-enabled/' + id;
    return this.http.put<Ticket>(url, params, options).pipe(
      map((response:any) => {
        return this.mapResponseToTicket(response);
      }),
      catchError(err => {
        console.log("err = ", err)
        return throwError(err);
      })
    )
  }

}
