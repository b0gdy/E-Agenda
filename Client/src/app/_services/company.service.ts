import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Employee} from "../_models/employee";
import {Company} from "../_models/company";
import {Client} from "../_models/client";
import {catchError, map, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  baseUrl = 'http://localhost:8080/companies';

  constructor(private http: HttpClient) { }

  create(model: any) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/create';
    let body = {
      name: model.name,
    }
    return this.http.post<Company>(url, body, options)
  }

  getAll() {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl;
    return this.http.get<Company[]>(url, options)
  }

  getById(id: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/' + id;
    return this.http.get<Company>(url, options)
  }

  update(id: number, model: any) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('name', model.name)
    let options = {headers: headers};
    let url = this.baseUrl + '/' + id
    return this.http.put<Company>(url, params, options)
  }

}
