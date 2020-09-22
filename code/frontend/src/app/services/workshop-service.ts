import {Router} from '@angular/router';
import {CookieService} from 'ngx-cookie-service';
import {Organizer, Workshop} from '../model/workshop/workshop';
import axios from 'axios';
import {environment} from '../../environments/environment';
import {Injectable} from '@angular/core';
import {City} from '../model/city/city';
import {LoginService} from './login-service';
import jwtDecode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})

/**
 * Service managing the workshops data retrieval from the database
 */
export class WorkshopService {
  readonly DUPLICATE_PREFIX = 'Copie de ';
  constructor(private router: Router, private cookieService: CookieService) {
  }
  /**
   * Retrieves all the workshops of the specified type
   * @param type the type of the workshops [all/draft/published]. (empty string = all)
   */
  async getWorkshops(type?: string): Promise<Workshop[]> {
    if (type === undefined) { type = ''; }
    const ws = [];
    console.log(this.cookieService.get(LoginService.TOKEN_KEY));
    await axios.get(environment.apiUrl + '/workshops/' + type, {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      response.data.forEach((workshop) => {
        const work = Workshop.toLocal(workshop);
        ws.push(work);
        console.log(work.ingredients);
      });
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return ws;
  }
  /**
   * Retrieves all the workshops of the specified type for a user
   * @param type the type of the workshops [all/draft/published]. (empty string = all)
   */
  async getWorkshopsRestricted() {
    const ws = [];
    await axios.get(
      environment.apiUrl + '/workshops/restricted'
    ).then((response) => {
      response.data.forEach((workshop) => {
        ws.push(workshop);
      });
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return ws;
  }

  async getWorkshopsSorted(sort, isConnected, type?) {
    const ws = [];
    let restr = '';
    if (type === undefined) { type = ''; }
    if (type !== '') { type += '/'; }
    if (!isConnected) { restr = 'restricted/'; } else { restr = type; }
    await axios.get(environment.apiUrl + '/workshops/' + restr + 'order/' + sort, {
        headers: {
          Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
        }
      }
    ).then((response) => {
      response.data.forEach((workshop) => {
        ws.push(workshop);
      });
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return ws;
  }

  async getWorkshopsFiltered(filter, type?: string) {
    const ws = [];
    const params = filter.name + '/' + filter.value;
    if (type === undefined) { type = ''; }
    if (type !== '') { type += '/'; }
    await axios.get(environment.apiUrl + '/workshops/' + type  + 'filter/' + params, {
        headers: {
          Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
        }
      }
    ).then((response) => {
      response.data.forEach((workshop) => {
        ws.push(workshop);
      });
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return ws;
  }

  /**
   * Retrieves all the locations (npa + city) from the db
   */
  async getLocations(): Promise<City[]> {
    const ws = [];
    await axios.get(environment.apiUrl + '/locations', {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      response.data.forEach((location) => {
        ws.push(location);
      });
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return ws;
  }

  /**
   * Retrieves all the categories
   */
  async getCategories(): Promise<string[]> {
    const ws = [];
    await axios.get(environment.apiUrl + '/categories', {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      response.data.forEach((category) => {
        ws.push(category.title);
      });
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return ws;
  }

  /**
   * Retrieves all the organizers
   */
  async getOrganizers(): Promise<Organizer[]> {
    const ws = [];
    await axios.get(environment.apiUrl + '/organizers', {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      response.data.forEach((organizer) => {
        ws.push(organizer);
      });
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return ws;
  }

  /**
   * Retrieves a particular workshop
   * @param titleWorkshop title of the workshop to get
   * @param dateWorkshop date of the workshop to get
   */
  async getWorkshop(titleWorkshop, dateWorkshop): Promise<Workshop> {
    let ws = null;
    await axios.get(environment.apiUrl + '/workshops/' + titleWorkshop + '/' + dateWorkshop, {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      },
    }).then((response) => {
      ws = Workshop.toLocal(response.data);
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return ws;
  }

  /**
   * Add a new workshop to the db
   * @param ws the workshop to add
   */
  async addWorkshop(ws: Workshop): Promise<boolean> {
    let status = false;
    await axios.post(environment.apiUrl + '/workshops/', Workshop.toProd(ws), {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      if (response.status === 200) {
        status = true;
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return status;
  }

  /**
   * Move a workshop from published to draft and conversely
   * @param ws the workshop to move to the other state
   */
  async transferWorkshop(ws: Workshop): Promise<boolean> {
    let status = false;
    await axios.post(environment.apiUrl + '/workshops/transfer/' + ws.state, Workshop.toProd(ws), {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      if (response.status === 200) {
        status = true;
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return status;
  }

  /**
   * Edit a workshop
   * @param ws the edited workshop
   * @param title the old title of the workshop
   * @param date the old date of the workshop
   */
  async editWorkshop(ws: Workshop, title, date): Promise<boolean> {
    const params = '/workshops/' + title + '/' + date;
    let status = false;
    await axios.put(environment.apiUrl + params, Workshop.toProd(ws),  {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      },
    }).then((response) => {
      if (response.status === 200) {
        status = true;
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return status;
  }

  /**
   * Remove a workshop
   * @param name title of the workshop
   * @param dateWorkshop date of the workshop
   */
  async removeWorkshop(name, dateWorkshop): Promise<boolean> {
    let status = false;
    const params = '/workshops/' + name + '/' + dateWorkshop;
    await axios.delete(environment.apiUrl + params, {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      },
    }).then((response) => {
      if (response.status === 200) {
        status = true;
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return status;
  }

  /**
   * Duplicate a workshop
   * @param oldTitle the title of the workshop
   * @param oldDate the date of the workshop
   */
  async duplicateWorkshop(oldTitle: string, oldDate: Date) {
    let status = false;
    const params = '/workshops/' + oldTitle + '/' + oldDate + '/duplicate';
    await axios.post(
      environment.apiUrl + params,
      {
        title: 'Copie de ' + oldTitle,
        date: oldDate
      },
      {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      },
    }).then((response) => {
      if (response.status === 200) {
        status = true;
      }
    }).catch((error) => {
      alert("Vérifier que l'atelier n'existe pas déjà.");
      // handle error
      console.log(error);
    });
    return status;
  }

  /**
   * Retrieve an organizer
   * @param email the email of the organizer
   */
  async getOrganizer(email: string) {
    let organizer = null;
    await axios.get(environment.apiUrl + '/organizers/' + email, {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      if (response.status === 200) {
        organizer = response.data;
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return organizer;
  }

  /**
   * Retrieve of a workshop
   * @param title the title of the workshop
   * @param date the date of the workshop
   */
  async getSubscriptions(title, date) {
    let subscriptions = [];
    const params = title + '/' + date + '/subscriptions';
    await axios.get(environment.apiUrl + '/workshops/' + params, {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      if (response.status === 200) {
        subscriptions = response.data;
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return subscriptions;
  }

  /**
   * Export the subscriptions of a workshop
   * @param title the title of the workshop
   * @param date the date of the workshop
   */
  async exportSubscriptions(title: string, date: string) {
    let blob = null;
    const params = title + '/' + date + '/subscriptions/export';
    await axios.get(environment.apiUrl + '/workshops/' + params, {
      responseType: 'arraybuffer',
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      if (response.status === 200) {
        console.log(response.data);
        blob = new Blob([response.data], {
          type: 'application/pdf',
        });
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return blob;
  }

  /**
   * Export the list of the workshops
   */
  async exportWokshops() {
    let value = null;
    await axios.get(environment.apiUrl + '/workshops/export', {
      responseType: 'arraybuffer',
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      if (response.status === 200) {
        console.log(response.data);
        value = new Blob([response.data], {
          type: 'application/pdf',
        });
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return value;
  }

  /**
   * Retrieve the groceries to buy from a start date to another
   * @param startDate the date where is begins
   * @param endDate the date where it ends
   */
  async getShoppingList(startDate: string, endDate: string) {
    let value = [];
    const params = startDate + '/' + endDate + '/';
    await axios.get(environment.apiUrl + '/shoppinglist/' + params, {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      if (response.status === 200) {
        value = response.data;
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return value;
  }

  /**
   * Export the shopping list
   * @param startDate the date where is begins
   * @param endDate the date where it ends
   */
  async exportShoppingList(startDate: string, endDate: string) {
    let value = null;
    const params = startDate + '/' + endDate;
    await axios.get(environment.apiUrl + '/shoppinglist/' + params + '/export', {
      responseType: 'arraybuffer',
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      }
    }).then((response) => {
      if (response.status === 200) {
        console.log(response.data);
        value = new Blob([response.data], {
          type: 'application/pdf',
        });
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return value;
  }

  /**
   * (Un)Subscribe a user to a workshop
   * @param title the title of the workshop
   * @param date the date of the workshop
   * @param subscribe if true, subcribe the user, else, unsubscribe him
   */
  async subscribe(title, date, subscribe: boolean) {
    let value = false;
    const tok = this.cookieService.get(LoginService.TOKEN_KEY);
    const params = title + '/' + date;
    const mail = jwtDecode(tok).sub;
    await axios.request({
      method: (subscribe) ? 'post' : 'delete',
      url: environment.apiUrl + '/workshops/' + params + '/subscriptions/' + mail,
      data: {},
      headers: {
        Authorization: tok
      }
    }).then((response) => {
      if (response.status === 200) {
        console.log(response.data);
        value = response.data;
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return value;
  }
  async isSubscribe(title, date) {
    let value = false;
    const tok = this.cookieService.get(LoginService.TOKEN_KEY);
    const params = title + '/' + date;
    const mail = jwtDecode(tok).sub;
    await axios.request({
      method: 'get',
      url: environment.apiUrl + '/workshops/' + params + '/subscriptions/' + mail,
      data: {},
      headers: {
        Authorization: tok
      }
    }).then((response) => {
      if (response.status === 200) {
        value = true;
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
    return value;
  }
}
