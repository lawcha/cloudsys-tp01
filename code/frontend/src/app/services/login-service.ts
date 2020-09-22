import {environment} from '../../environments/environment';
import axios from 'axios';
import {Router} from '@angular/router';
import {Injectable} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';
import {HomeComponent} from '../home/home.component';
import jwtDecode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
/**
 * Service to log or register a user to the database
 */
export class LoginService {
  static readonly TOKEN_KEY: string = '4KH12PT7C5'; // Random Key for the cookie (token)
  constructor(private router: Router, private cookieService: CookieService) {
  }

  static redirect(cookieService, router) {
    const token = cookieService.get(LoginService.TOKEN_KEY);
    if (token === '') {
      router.navigate(['']);
    } else {
      const roles = jwtDecode(token).roles;
      if (roles.includes(HomeComponent.ADMIN_ROLE)) {
        router.navigate(['/admin']);
      } else {
        router.navigate(['']);
      }
    }
  }

  /**
   * Register the given user to the database
   * @param user the user to add
   */
  async register(user): Promise<boolean> {
    let status = false;
    await axios.post(environment.apiUrl + '/users/signup', {
      email: user.email,
      password: user.password,
      firstName: user.firstName,
      lastName: user.lastName,
      phone: user.phone,
      street: user.street,
      location: {
        npa: user.location.npa,
        city: user.location.city
      }
    }).then((response) => {
      status = true;
      this.cookieService.set(LoginService.TOKEN_KEY, response.headers.authorization);
    }).catch((error) => {
      alert('Impossible de s\'senregistrer avec ces informations.');
      // handle error
      console.log(error);
    });
    return status;
  }

  /**
   * Verify and get the token of the user if he is authorized to login
   * @param user the username
   * @param passw the password
   */
  async login(user, passw): Promise<boolean> {
    let status = false;
    await axios.post(environment.apiUrl + '/users/login', {
      email: user,
      password: passw
    }).then((response) => {
      status = true;
      this.cookieService.set(LoginService.TOKEN_KEY, response.headers.authorization);
    }).catch((error) => {
      alert('Identifiants incorrects');
      // handle error
      console.log(error);
    });
    return status;
  }

  /**
   * Logout the user from the app
   */
  logout(): void {
    axios.post(environment.apiUrl + '/users/logout', null, {
      headers: {
        Authorization: this.cookieService.get(LoginService.TOKEN_KEY)
      },
    }).then((response) => {
      if (response.status === 200) {
        this.cookieService.delete(LoginService.TOKEN_KEY, '/');
        this.router.navigate(['/']);
      }
    }).catch((error) => {
      // handle error
      console.log(error);
    });
  }
}
