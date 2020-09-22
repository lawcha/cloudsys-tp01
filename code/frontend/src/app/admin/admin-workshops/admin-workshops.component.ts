import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {LoginService} from '../../services/login-service';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {WorkshopService} from '../../services/workshop-service';
import {Workshop} from '../../model/workshop/workshop';
import {DatePipe} from '@angular/common';
import {environment} from '../../../environments/environment';
import {SubscriptionsComponent} from '../../modal/subscriptions/subscriptions.component';
import {ShoppingListComponent} from '../../modal/shopping-list/shopping-list.component';
import FileSaver from 'file-saver';
import {CookieService} from 'ngx-cookie-service';

export interface Type {
  value: string;
  text: string;
}
@Component({
  selector: 'app-adminworkshops',
  templateUrl: './admin-workshops.component.html',
  styleUrls: ['./admin-workshops.component.css']
})
/**
 * Home page when connected as admin. It lists the workshops and actions related to them.
 */
export class AdminWorkshopsComponent implements OnInit {
  readonly TYPES: Type[] = [
    {text: 'Tout', value: ''},
    {text: 'Brouillon', value: 'draft'},
    {text: 'Publié', value: 'published'},
    {text: 'Terminé', value: 'closed'},
  ];
  type = this.TYPES[0].value;
  workshops: Workshop[] = [];
  displayedColumns: string[] = ['state', 'title', 'category', 'participants', 'dateWorkshop', 'inscription', 'actions'];

  dataSource: MatTableDataSource<Workshop>;

  constructor(
    private loginService: LoginService,
    private router: Router,
    private workshopService: WorkshopService,
    public datePipe: DatePipe,
    private dialog: MatDialog,
    private cookieService: CookieService
  ) { }

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  filtersShown = false;
  categories: string[] = [];
  category = '';
  sort  = '';
  readonly SORTS = [
    {text: 'Titre', value: 'title'},
    {text: 'Date', value: 'date'},
  ];

  ngOnInit(): void {
    LoginService.redirect(this.cookieService, this.router);
    this.getWorkshops();
    this.workshopService.getCategories().then(categories => {
      categories.forEach((category) => {
        this.categories.push(category);
      });
    });
  }

  /**
   * Retrieves the workshops of the selected type
   */
  getWorkshops() {
    this.sort = '';
    this.category = '';
    this.workshopService.getWorkshops(this.type).then((workshops) => {
      this.workshops = workshops;
      this.dataSource = new MatTableDataSource<Workshop>(this.workshops);
      this.dataSource.paginator = this.paginator;
    });
  }

  getWorkshopsSort() {
    this.category = '';
    this.workshopService.getWorkshopsSorted(this.sort, true, this.type).then((w) => {
      this.workshops = w;
      this.dataSource = new MatTableDataSource<Workshop>(this.workshops);
      this.dataSource.paginator = this.paginator;
    });
  }
  getWorkshopsFilter() {
    this.sort = '';
    this.workshopService.getWorkshopsFiltered({name: 'category', value: this.category}, this.type).then((w) => {
      this.workshops = w;
      this.dataSource = new MatTableDataSource<Workshop>(this.workshops);
      this.dataSource.paginator = this.paginator;
    });
  }

  /**
   * Navigate to the "addWorkshop" page
   */
  addWorkshop() {
    this.router.navigate(['/admin/add']);
  }

  /**
   * Navigate to the edit page of the workshop
   * @param index the index of the workshop
   */
  editWorkshop(index) {
    this.router.navigate([
      '/admin/edit/',
      {
        title: this.workshops[index].title,
        date: this.workshops[index].date,
      }
    ]);
  }

  /**
   * Remove the specified workshop
   * @param index the index of the workshop to remove
   */
  removeWorkshop(index) {
    const status = confirm('Voulez-vous vraiment supprimer cet atelier ?');
    if (status) {
      this.workshopService.removeWorkshop(this.workshops[index].title, this.workshops[index].date)
        .then(() => {
          this.getWorkshops();
        });
    }
  }

  /**
   * Navigate to the view page of the workshop
   * @param workshop the workshop where it goes
   */
  viewWorkshop(workshop) {
    this.router.navigate([
      '/admin/view/',
      {
        title: workshop.title,
        date: workshop.date,
      }
    ]);
  }

  /**
   * Duplicate a workshop specified by the given ID
   * @param index
   */
  duplicateWorkshop(index) {
    const status = confirm('Voulez-vous dupliquer cet atelier ?');
    if (status) {
      this.workshopService.duplicateWorkshop(this.workshops[index].title, this.workshops[index].date)
        .then(() => {
          this.getWorkshops();
        });
    }
  }

  /**
   * Export the list of the workshops
   */
  exportWorkshops() {
    this.workshopService.exportWokshops()
      .then((blob) => {
        FileSaver.saveAs(blob, 'list.pdf');
      });
  }

  /**
   * Show the shopping list modal
   */
  showShoppingList() {
    environment.activeDialog = this.dialog.open(ShoppingListComponent, {
      panelClass: 'shop-dialog',
      width: '600px'
    });
  }
}
