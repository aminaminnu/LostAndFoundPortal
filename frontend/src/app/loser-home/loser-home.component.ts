import { Component, OnInit } from '@angular/core';
import { FoundItem, FoundItemService } from '../services/found-item.service';

@Component({
  selector: 'app-loser-home',
  templateUrl: './loser-home.component.html',
  styleUrls: ['./loser-home.component.css']
})
export class LoserHomeComponent implements OnInit {

  foundItems: FoundItem[] = [];

  constructor(private foundItemService: FoundItemService) {}

  ngOnInit(): void {
    this.loadSuggestedItems();
  }

  loadSuggestedItems(): void {
  this.foundItemService.getSuggestedItems().subscribe({
    next: (data: any[]) => {
      this.foundItems = data.map(item => ({
        ...item,
        imageUrl: item.imagePath ? `http://localhost:8080${item.imagePath}` : 'assets/default-image.png'
      }));
    },
    error: (err: any) => {
      console.error('Failed to load suggested items:', err);
    }
  });
}

}
