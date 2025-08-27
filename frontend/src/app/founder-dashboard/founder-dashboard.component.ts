import { Component } from '@angular/core';

@Component({
  selector: 'app-founder-dashboard',
  templateUrl: './founder-dashboard.component.html',
  styleUrls: ['./founder-dashboard.component.css']
})
export class FounderDashboardComponent {
  userName = 'Amina'; // You can dynamically bind this from auth

  foundItemsCount = 5;
  pendingClaimsCount = 2;

  barChartData = {
    labels: ['Found Items', 'Pending Claims'],
    datasets: [
      {
        label: 'Dashboard Stats',
        data: [this.foundItemsCount, this.pendingClaimsCount],
        backgroundColor: ['#3f51b5', '#e91e63'],
      },
    ],
  };

  barChartOptions = {
    responsive: true,
    scales: {
      y: {
        beginAtZero: true,
        precision: 0
      }
    }
  };

}
