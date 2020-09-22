import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {WorkshopService} from '../../services/workshop-service';
import FileSaver from 'file-saver';

@Component({
  selector: 'app-subscriptions',
  templateUrl: './subscriptions.component.html',
  styleUrls: ['./subscriptions.component.css']
})
export class SubscriptionsComponent implements OnInit {
  subscriptions: string[] = [];

  constructor(
    public dialogRef: MatDialogRef<SubscriptionsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SubscriptionData,
    private workshopService: WorkshopService
  ) {}

  ngOnInit(): void {
    this.workshopService.getSubscriptions(this.data.title, this.data.date).then((subs) => {
      subs.forEach((sub) =>  {
        this.subscriptions.push(sub.firstName + ' ' + sub.lastName + ' (' + sub.email + ')');
      });
    });
  }

  /**
   * Export the subscriptions and download the file
   */
  exportSubs() {
    this.workshopService.exportSubscriptions(this.data.title, this.data.date).then(blob => {
      FileSaver.saveAs(blob, 'list.pdf');
    });
  }
}

/**
 * Input data of the modal
 */
export interface SubscriptionData {
  title: string;
  date: string;
}
