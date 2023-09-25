import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Role} from "../_models/role";
import {map} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  baseUrl = 'http://localhost:8080/roles';
  // baseUrl = 'http://authentication:8080/roles';

  constructor(private http: HttpClient) { }

  getAll() {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': "Bearer " + token
    });
    let options = {headers: headers};
    let url = this.baseUrl;
    return this.http.get<Role[]>(url, options)
  }

  getByName(name: string) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': "Bearer " + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/getbyname/' + name;
    return this.http.get<Role>(url, options)
  }


}
