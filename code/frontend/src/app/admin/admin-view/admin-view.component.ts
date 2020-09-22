import {Component, Input, OnInit} from '@angular/core';
import {Organizer, Workshop} from '../../model/workshop/workshop';
import {WorkshopService} from '../../services/workshop-service';
import {ActivatedRoute, Router} from '@angular/router';
import { DatePipe } from '@angular/common';
import {City} from '../../model/city/city';
import {environment} from '../../../environments/environment';
import {LoginComponent} from '../../modal/login/login.component';
import {MatDialog} from '@angular/material/dialog';
import {SubscriptionsComponent} from '../../modal/subscriptions/subscriptions.component';
@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrls: ['./admin-view.component.css']
})
/**
 * Page which show the details of a workshop
 */
export class AdminViewComponent implements OnInit {
  // Request parameters
  title: string;
  date: Date;
  // --- end
  organizer: Organizer = {firstName: '', lastName: '', phone: '', email: '', street: '', location: new City(0, '')};
  workshop: Workshop = new Workshop(
    '', '', '',  null,  null, '', new City(1000, ''), '', [], 1, 1, '', 'draft'
  );

  readonly STATES = {
    draft: 'Brouillon',
    published: 'Publié',
    closed: 'Terminé',
  };
  constructor(
    private workshopService: WorkshopService,
    private actRoute: ActivatedRoute,
    public datePipe: DatePipe,
    private router: Router,
    public dialog: MatDialog
  ) {
    this.title = this.actRoute.snapshot.params.title;
    this.date = this.actRoute.snapshot.params.date;
  }

  ngOnInit(): void {
    this.workshopService.getWorkshop(this.title, this.date).then((data) => {
      // Copy the data to the workshop field
      this.workshop.title = data.title;
      this.workshop.state = data.state;
      this.workshop.ingredients = data.ingredients;
      this.workshop.description = data.description;
      this.workshop.image = data.image;
      this.workshop.maxParticipants = data.maxParticipants;
      this.workshop.minParticipants = data.minParticipants;
      this.workshop.location = data.location;
      this.workshop.street = data.street;
      this.workshop.inscriptionLimit = data.inscriptionLimit;
      this.workshop.emailOrganizer = data.emailOrganizer;
      this.workshop.category = data.category;
      this.workshop.date = data.date;
      this.workshopService.getOrganizer(this.workshop.emailOrganizer).then(organizer => {
        this.organizer = organizer;
      });
    });
  }

  /**
   * Go back to the list of the workshops
   */
  goBack() {
    this.router.navigate(['admin']);
  }

  /**
   * Open the modal which shows the subscriptions of the workshop
   */
  openSubscriptions() {
    environment.activeDialog = this.dialog.open(SubscriptionsComponent, {
      panelClass: 'subs-dialog',
      width: '600px',
      data: {
        title: this.title,
        date: this.date
      }
    });
  }
}
