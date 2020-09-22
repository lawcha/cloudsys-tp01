import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import {Router} from '@angular/router';
import {LoginService} from '../services/login-service';
import {CookieService} from 'ngx-cookie-service';
import {Workshop} from '../model/workshop/workshop';
import {WorkshopService} from '../services/workshop-service';
import {DatePipe} from '@angular/common';
import jwtDecode from 'jwt-decode';
import {Type} from '../admin/admin-workshops/admin-workshops.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
/**
 * Home page of the app. It contains a list of the workshop. The level of details shown depends if the user is
 * connected or not.
 */
export class HomeComponent implements OnInit {
  // Constants
  static ADMIN_ROLE = 'admin';
  static USER_ROLE = 'client';
  readonly SORTS = [
    {text: 'Titre', value: 'title'},
    {text: 'Date', value: 'date'},
  ];
  readonly TYPES: Type[] = [
    {text: 'Tout', value: ''},
    {text: 'Terminé', value: 'closed'},
    {text: 'Publié', value: 'published'},
  ];
  // Attributes
  categories: string[] = [];
  workshops: Workshop[] = [];
  isConnected = false;
  sort = '';
  type = 'published';
  category = '';
  filtersShown = false;
  constructor(
    private router: Router,
    private cookieService: CookieService,
    private workshopService: WorkshopService,
    public datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    const token = this.cookieService.get(LoginService.TOKEN_KEY);
    if (token !== '') {
      const roles = this.getAutorizations(token);
      if (roles.includes(HomeComponent.ADMIN_ROLE)) {
        this.router.navigate(['/admin']);
      } else if (roles.includes(HomeComponent.USER_ROLE)) {
        this.isConnected = true;
        this.workshopService.getCategories().then(categories => {
          categories.forEach((category) => {
            this.categories.push(category);
          });
        });
      }
    }
    this.getWorkshops();
  }

  /**
   * Retrieve the workshops
   */
  getWorkshops() {
    if (this.isConnected) {
      this.workshopService.getWorkshops(this.type).then((w) => {
        this.workshops = w;
      });
    } else {
      this.workshopService.getWorkshopsRestricted().then((w) => {
        this.workshops = w;
      });
    }
  }

  getWorkshopsSort() {
    this.category = '';
    this.workshopService.getWorkshopsSorted(this.sort, this.isConnected, this.type).then((w) => {
      this.workshops = w;
    });
  }
  getWorkshopsFilter() {
    this.sort = '';
    this.workshopService.getWorkshopsFiltered({name: 'category', value: this.category}, this.type).then((w) => {
      this.workshops = w;
    });
  }

  /**
   * Decode and return the roles from a token
   * @param token the token to decode
   */
  getAutorizations(token: string): string[] {
    return jwtDecode(token).roles;
  }

  /**
   * Go to the "viewWorkshop" page
   * @param wsTitle the title of the workshop
   * @param wsDate the date of the workshop
   */
  openDetails(wsTitle, wsDate) {
    this.router.navigate(['/view',
      {
        title: wsTitle,
        date: wsDate,
      }
      ]);
  }
}
