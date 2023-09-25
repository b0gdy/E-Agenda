import { Injectable } from '@angular/core';
import {Employee} from "../_models/employee";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {UserService} from "./user.service";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {


  baseUrl = 'http://localhost:8080/employees';
  // baseUrl = 'http://authentication:8080/employees';

  constructor(private http: HttpClient, private userService: UserService) { }

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
      role: model.role,
      firstName: model.firstName,
      lastName: model.lastName,
      position: model.position,
      salary: model.salary
    }
    return this.http.post<Employee>(url, body, options)
  }

  activateAccount(username: string) {
    let url = this.baseUrl + '/activate-account/' + username;
    console.log("url = ", url);
    return this.http.get<Employee>(url);
  }

  getAll() {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl;
    return this.http.get<Employee[]>(url, options)
  }

  getAllEnabled() {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/enabled';
    return this.http.get<Employee[]>(url, options)
  }

  getById(id: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/' + id;
    return this.http.get<Employee>(url, options)
  }

  update(id: number, model: any) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('firstName', model.firstName)
      .set('lastName', model.lastName)
      .set('position', model.position)
      .set('salary', model.salary)
      .set('role', model.role)
      .set('password', model.password);
    console.log("firstName = ", params.get("firstName"));
    console.log("lastName = ", params.get("lastName"));
    console.log("position = ", params.get("position"));
    console.log("salary = ", params.get("salary"));
    console.log("role = ", params.get("role"));
    console.log("password = ", params.get("password"));
    let options = {headers: headers};
    let url = this.baseUrl + '/' + id
    return this.http.put<Employee>(url, params, options)
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
    return this.http.put<Employee>(url, params, options)
  }


  changePassword(id: number, password: string) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('password', password);
    let options = {headers: headers};
    let url = this.baseUrl + '/changePassword/' + id;
    return this.http.put<Employee>(url, params, options)
  }

}
