import {Component, OnInit} from '@angular/core';
import {User} from "./_models/user";
import {UserService} from "./_services/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'Client';

  constructor(private userService: UserService) {}

  ngOnInit(){
    this.setCurrentUser();
  }

  setCurrentUser() {
    const userString= localStorage.getItem('user');
    if(userString) {
      const user: User = JSON.parse(userString);
      this.userService.setCurrentUser(user);
    }
  }

}
