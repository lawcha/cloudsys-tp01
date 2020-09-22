import {CommonModule, DatePipe} from '@angular/common';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {RouterModule, Routes} from '@angular/router';
import { LoginComponent } from './modal/login/login.component';
import {FlexLayoutModule} from '@angular/flex-layout';
import { HomeComponent } from './home/home.component';
import {MatDividerModule} from '@angular/material/divider';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatStepperModule} from '@angular/material/stepper';
import {MatTabsModule} from '@angular/material/tabs';
import { AdminWorkshopsComponent } from './admin/admin-workshops/admin-workshops.component';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatIconModule} from '@angular/material/icon';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatSortModule} from '@angular/material/sort';
import { AdminAddComponent } from './admin/admin-add/admin-add.component';
import {MatSelectModule} from '@angular/material/select';
import { HeaderElementComponent } from './header-element/header-element.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import {NgxMatDatetimePickerModule, NgxMatNativeDateModule} from '@angular-material-components/datetime-picker';
import { AdminEditComponent } from './admin/admin-edit/admin-edit.component';
import { WorkshopFormComponent } from './admin/workshop-form/workshop-form.component';
import {AdminViewComponent} from './admin/admin-view/admin-view.component';
import {MatChipsModule} from '@angular/material/chips';
import { SubscriptionsComponent } from './modal/subscriptions/subscriptions.component';
import {MatListModule} from '@angular/material/list';
import {MatTooltipModule} from '@angular/material/tooltip';
import {CKEditorModule} from '@ckeditor/ckeditor5-angular';
import { ShoppingListComponent } from './modal/shopping-list/shopping-list.component';
import { WorkshopViewComponent } from './workshop-view/workshop-view.component';

const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'view', component: WorkshopViewComponent },
  { path: 'admin', component: AdminWorkshopsComponent },
  { path: 'admin/add', component: AdminAddComponent },
  { path: 'admin/edit', component: AdminEditComponent },
  { path: 'admin/view', component: AdminViewComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    AdminWorkshopsComponent,
    AdminAddComponent,
    HeaderElementComponent,
    AdminEditComponent,
    WorkshopFormComponent,
    AdminViewComponent,
    SubscriptionsComponent,
    ShoppingListComponent,
    WorkshopViewComponent
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatDialogModule,
    RouterModule.forRoot(
      appRoutes, {onSameUrlNavigation: 'reload'}
    ),
    FlexLayoutModule,
    MatDividerModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatStepperModule,
    MatTabsModule,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    FormsModule,
    MatSortModule,
    MatSelectModule,
    NgxMatDatetimePickerModule,
    NgxMatNativeDateModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    MatNativeDateModule,
    MatChipsModule,
    MatListModule,
    MatTooltipModule,
    CKEditorModule
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
