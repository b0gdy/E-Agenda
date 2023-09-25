import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { User } from '../_models/user';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model:any = {}

  constructor(public userService: UserService, private router: Router, private toastr: ToastrService) { }

  ngOnInit(): void {
  }

  login() {
    this.userService.login(this.model).subscribe({
      next: (response) => {
        this.router.navigateByUrl('/user')
      },
      error: (error) => {
        this.toastr.error(error.error);
      },
      complete: () => {
        this.toastr.success("Login successful!")
      }
    })
      
      
  }

}
