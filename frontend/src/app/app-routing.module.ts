// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { FounderDashboardComponent } from './founder-dashboard/founder-dashboard.component';
import { FounderFoundItemsComponent } from './founder-found-items/founder-found-items.component';
import { FounderFoundItemFormComponent } from './founder-found-item-form/founder-found-item-form.component';
import { SearchLostItemsComponent } from './search-lost-items/search-lost-items.component';
import { NotificationComponent } from './notification/notification.component';
import { FinderHomeComponent } from './finder-home/finder-home.component';
import { AuthGuard } from './auth/auth.guard';
import { LoserDashboardComponent } from './loser-dashboard/loser-dashboard.component';
import { LoserLoseItemsComponent } from './loser-lose-items/loser-lose-items.component';
import { LoserLoseItemFormComponent } from './loser-lose-item-form/loser-lose-item-form.component';
import { LoserHomeComponent } from './loser-home/loser-home.component';
import { ClaimListComponent } from './claim-list/claim-list.component';
import { ClaimFormComponent } from './claim-form/claim-form.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ReporterDashboardComponent } from './reporter-dashboard/reporter-dashboard.component'; // ✅ added

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },

  {
    path: 'admin-dashboard',
    component: AdminDashboardComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_ADMIN'] }
  },

  { path: 'finder/dashboard', component: FounderDashboardComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'FINDER' } },
  { path: 'founder/my-found-items', component: FounderFoundItemsComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'FINDER' }},
  { path: 'founder/add-found-item', component: FounderFoundItemFormComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'FINDER' }},
  { path: 'founder/edit-found-item/:id', component: FounderFoundItemFormComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'FINDER' }},
  { path: 'founder/search-lost-items', component: SearchLostItemsComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'FINDER' }},
  { path: 'founder/notifications', component: NotificationComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'FINDER' }},

  { path: 'finder-home', component: FinderHomeComponent },

  { path: 'loser/dashboard', component: LoserDashboardComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'LOSER' }},
  { path: 'lost/my-lost-items', component: LoserLoseItemsComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'LOSER' }},
  { path: 'lost/add-lost-item', component: LoserLoseItemFormComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'LOSER' }},
  { path: 'loser/edit-lost-item/:id', component: LoserLoseItemFormComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'LOSER' }},
  { path: 'lost/search-found-items', component: SearchLostItemsComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'LOSER' }},

  { path: 'loser-home', component: LoserHomeComponent },

  // ✅ Corrected Reporter Dashboard Route
  {
    path: 'reporter-dashboard',
    component: ReporterDashboardComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_REPORTER'] }
  },

  { path: 'submit-claim', component: ClaimFormComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_USER'], userType: 'LOSER' }},
  { path: 'claim-list', component: ClaimListComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
