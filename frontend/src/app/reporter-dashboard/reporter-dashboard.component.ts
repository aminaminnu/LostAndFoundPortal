// src/app/reporter-dashboard/reporter-dashboard.component.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-reporter-dashboard',
  templateUrl: './reporter-dashboard.component.html',
  styleUrls: ['./reporter-dashboard.component.css']
})
export class ReporterDashboardComponent {
  active: 'overview' | 'claims' | 'lost' | 'found' = 'overview';
}
