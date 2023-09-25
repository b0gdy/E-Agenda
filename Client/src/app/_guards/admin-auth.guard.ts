import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { map, Observable } from 'rxjs'
import { UserService } from '../_services/user.service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class AdminAuthGuard implements CanActivate {

  constructor(private userService: UserService, private toastr: ToastrService) {}

  canActivate(): Observable<boolean> {
    return this.userService.currentUser$.pipe(
      map(user => {
        if (!user) return false;
        if (user.role == 'admin') {
          return true;
        } else {
          this.toastr.error('Access denied!');
          return false;
        }
      })
    )
  }
  
}
