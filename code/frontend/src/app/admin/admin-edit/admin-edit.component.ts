import { Component, OnInit } from '@angular/core';
import {Workshop} from '../../model/workshop/workshop';
import {WorkshopService} from '../../services/workshop-service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-admin-edit',
  templateUrl: './admin-edit.component.html',
  styleUrls: ['./admin-edit.component.css']
})
/**
 * Component to edit a workshop of the app. It simply call the component "WorkshopForm"
 */
export class AdminEditComponent implements OnInit {
  label = 'MODIFIER';
  title: string;
  date: Date;
  workshop = new Workshop(
    'Titre', '', '', null, null, '', null, '', [], 0, 0, '', ''
  );
  constructor(private workshopService: WorkshopService, private actRoute: ActivatedRoute) {
    this.title = this.actRoute.snapshot.params.title;
    this.date = this.actRoute.snapshot.params.date;
  }

  ngOnInit(): void {
    // this.workshopService.getWorkshop(this.title, this.date).then(value => {
    //   this.workshop = value;
    // });
  }

}
