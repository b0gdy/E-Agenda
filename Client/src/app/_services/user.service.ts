import { Injectable } from '@angular/core';
import { User } from '../_models/user';
import {BehaviorSubject, catchError, ReplaySubject, throwError} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { map } from 'rxjs/operators';
import {Employee} from "../_models/employee";
import {UserFull} from "../_models/UserFull";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  baseUrl = 'http://localhost:8080';
  // baseUrl = 'http://authentication:8080';

  private currentUserSource = new BehaviorSubject<User | null>(null);
  currentUser$ = this.currentUserSource.asObservable();

  constructor(private http: HttpClient) { }

  login(model: any) {
    let url = this.baseUrl + '/login'
    return this.http.post<User>(url, model).pipe(
      map((response: User) => {
        const user = response;
        if (user) {
          localStorage.setItem('user', JSON.stringify(user));
          localStorage.setItem('id', JSON.stringify(user.id));
          localStorage.setItem('userName', user.userName);
          localStorage.setItem('role', user.role);
          localStorage.setItem('token', user.jwtToken);
          this.currentUserSource.next(user);
        }
      }),
      catchError(err => {
        return throwError(err);
      })
    )
  }

  setCurrentUser(user: User) {
    this.currentUserSource.next(user);
  }

  logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('id');
    localStorage.removeItem('userName');
    localStorage.removeItem('role');
    localStorage.removeItem('token');
    this.currentUserSource.next(null);
  }

  disable(id: number) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': "Bearer " + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + '/disableuser/' + id;
    return this.http.put<User>(url, options)
  }




  getAll() {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + "/users";
    return this.http.get<UserFull[]>(url, options)
  }

  getAllEnabled() {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + "/users/enabled";
    return this.http.get<UserFull[]>(url, options)
  }

  getById(id: number) {
    console.log("service.getById");
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + "/users/" + id;
    console.log("url = ", url);
    return this.http.get<UserFull>(url, options)
  }

  getByUsername(username: string) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let options = {headers: headers};
    let url = this.baseUrl + "/users/" + username;
    return this.http.get<UserFull>(url, options)
  }

  update(id: number, model: any) {
    let token = localStorage.getItem('token');
    let headers = new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
    let params = new HttpParams()
      .set('firstName', model.firstName)
      .set('lastName', model.lastName)
      .set('password', model.password);
    let options = {headers: headers};
    let url = this.baseUrl + "/users/" + id;
    return this.http.put<UserFull>(url, params, options)
  }

}
