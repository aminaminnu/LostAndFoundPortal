import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';



import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './home/home.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { FounderDashboardComponent } from './founder-dashboard/founder-dashboard.component';
import { FounderFoundItemsComponent } from './founder-found-items/founder-found-items.component';
import { FounderFoundItemFormComponent } from './founder-found-item-form/founder-found-item-form.component';
import { SearchLostItemsComponent } from './search-lost-items/search-lost-items.component';
import { NotificationComponent } from './notification/notification.component';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatLabel } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { FinderHomeComponent } from './finder-home/finder-home.component'; 
import { AuthInterceptor } from './auth/auth.interceptor';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LoserHomeComponent } from './loser-home/loser-home.component';
import { LoserLoseItemFormComponent } from './loser-lose-item-form/loser-lose-item-form.component';
import { LoserLoseItemsComponent } from './loser-lose-items/loser-lose-items.component';
import { LoserDashboardComponent } from './loser-dashboard/loser-dashboard.component';
import { ClaimListComponent } from './claim-list/claim-list.component';
import { ReporterDashboardComponent } from './reporter-dashboard/reporter-dashboard.component';
import { ClaimFormComponent } from './claim-form/claim-form.component';
import { NgChartsModule } from 'ng2-charts';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { MatListModule }     from '@angular/material/list';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule }   from '@angular/material/select';
import { MatTooltipModule }  from '@angular/material/tooltip';
import { MatOptionModule } from '@angular/material/core'; 
import { MatDialogModule } from '@angular/material/dialog'; 


@NgModule({

  
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    FounderDashboardComponent,
    FounderFoundItemsComponent,
    FounderFoundItemFormComponent,
    SearchLostItemsComponent,
    NotificationComponent,
    FinderHomeComponent,
    LoserHomeComponent,
    LoserLoseItemFormComponent,
    LoserLoseItemsComponent,
    LoserDashboardComponent,
    ClaimListComponent,
    ReporterDashboardComponent,
    ClaimFormComponent,
    AdminDashboardComponent
   
  ],
  imports: [
     BrowserAnimationsModule,
    ReactiveFormsModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule ,
    MatPaginatorModule,
    MatProgressSpinnerModule,
     NgChartsModule,
     MatSnackBarModule,
     MatListModule,
     MatCheckboxModule,
     MatSelectModule,
     MatTooltipModule,
     MatOptionModule,
     MatDialogModule
     
  ],
  
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
