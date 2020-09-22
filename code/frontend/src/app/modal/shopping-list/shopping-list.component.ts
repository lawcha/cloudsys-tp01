import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {SubscriptionData} from '../subscriptions/subscriptions.component';
import {WorkshopService} from '../../services/workshop-service';
import {DatePipe} from '@angular/common';
import {Ingredient, Workshop} from '../../model/workshop/workshop';
import FileSaver from 'file-saver';

interface Shopping {
  title: string;
  date: Date;
  ingredients: Ingredient[];
}
@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.css']
})
/**
 * Show the shopping list for a specified date range
 */
export class ShoppingListComponent implements OnInit {
  startDate: Date = new Date();
  endDate: Date = new Date();
  shoppingList: Shopping[] = [];
  constructor(
    public dialogRef: MatDialogRef<ShoppingListComponent>,
    private workshopService: WorkshopService,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    this.endDate.setDate(this.endDate.getDate() + 10); // Set end 10 days after
    this.getShoppingList();
  }

  /**
   * Get the shopping list at the date range
   */
  getShoppingList() {
    const start = this.datePipe.transform(this.startDate, 'yyy-MM-dd');
    const end = this.datePipe.transform(this.endDate, 'yyy-MM-dd');
    this.workshopService.getShoppingList(start, end).then(shoppingList => {
      for (const shop of shoppingList) {
        this.shoppingList.push(
          {
            title: shop.title,
            date: shop.date,
            ingredients: Workshop.formatToJSON(shop.ingredients)
          }
        );
      }
    });
  }

  /**
   * Export the list and download it
   */
  exportList() {
    const start = this.datePipe.transform(this.startDate, 'yyy-MM-dd');
    const end = this.datePipe.transform(this.endDate, 'yyy-MM-dd');
    this.workshopService.exportShoppingList(start, end).then(blob => {
      FileSaver.saveAs(blob, 'list.pdf');
    });
  }
}
