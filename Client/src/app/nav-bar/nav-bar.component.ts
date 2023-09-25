import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {User} from "../_models/user";
import {take} from "rxjs";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  user: User = {} as User;

  protected readonly localStorage: Storage = localStorage;

  constructor(public userService: UserService, private router: Router) {
    this.userService.currentUser$.pipe(take(1)).subscribe({
      next: user => {
        if (user) {
          this.user = user;
        }
      }
    })
  }


  ngOnInit(): void {
  }

  logout() {
    this.userService.logout();
    this.router.navigateByUrl('/');
  }

}
