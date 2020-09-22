import {Component, OnInit} from '@angular/core';
import {LoginService} from '../../services/login-service';
import {Router} from '@angular/router';
import {environment} from '../../../environments/environment';
import {WorkshopService} from '../../services/workshop-service';
import {City} from '../../model/city/city';
import {HomeComponent} from '../../home/home.component';
import {CookieService} from 'ngx-cookie-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
/**
 * Login form to connect or register
 */
export class LoginComponent implements OnInit {
  user = {
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    phone: '',
    street: '',
    location: City,
  };
  locations: City[] = [];

  constructor(private loginService: LoginService, private router: Router, private workshopService: WorkshopService, private cookieService: CookieService) {
  }

  ngOnInit(): void {
    this.workshopService.getLocations().then(locations => {
      locations.forEach((location) => {
        this.locations.push(location);
      });
    });
  }

  /**
   * Log the user in
   */
  async login() {
    const isLogged: boolean = await this.loginService.login(this.user.email, this.user.password);
    if (isLogged) {
      if (environment.activeDialog != null) {
        environment.activeDialog.close();
        LoginService.redirect(this.cookieService, this.router);
      }
      environment.activeDialog = null;
    }
  }

  /**
   * Register the user
   */
  async register() {
    const isLogged: boolean = await this.loginService.register(this.user);
    if (isLogged) {
      if (environment.activeDialog != null) {
        environment.activeDialog.close();
        LoginService.redirect(this.cookieService, this.router);
      }
      environment.activeDialog = null;
    }
  }
}
