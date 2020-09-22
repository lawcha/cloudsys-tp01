import { Component, OnInit } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {LoginService} from '../services/login-service';
import {environment} from '../../environments/environment';
import {LoginComponent} from '../modal/login/login.component';
import {CookieService} from 'ngx-cookie-service';
import {ActivatedRoute, Router} from '@angular/router';
@Component({
  selector: 'app-header-element',
  templateUrl: './header-element.component.html',
  styleUrls: ['./header-element.component.css']
})
/**
 * Represent the base structure of the app. It contains the header of the pages and the body where each page is added
 * dynamically
 */
export class HeaderElementComponent implements OnInit {
  title = 'frontend';
  env = environment;
  token = '';
  constructor(public dialog: MatDialog, private loginService: LoginService, private cookieService: CookieService, private router: Router) {
  }

  ngOnInit(): void {
    this.router.events.subscribe((val) => {
      this.token = this.cookieService.get(LoginService.TOKEN_KEY);
    });
  }

  /**
   * Open the modal of the login
   */
  openLoginDialog(): void {
    environment.activeDialog = this.dialog.open(LoginComponent, {
      panelClass: 'login-dialog',
      width: '600px',
    });
  }

  /**
   * Logout the user (or admin)
   */
  logout(): void {
    this.loginService.logout();
  }

}
