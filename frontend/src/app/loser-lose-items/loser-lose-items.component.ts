import { Component, OnInit, ViewChild, AfterViewInit, Input } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { LostItem, LoserService } from '../services/loser.service';

@Component({
  selector: 'app-loser-lose-items',
  templateUrl: './loser-lose-items.component.html',
  styleUrls: ['./loser-lose-items.component.css']
})
export class LoserLoseItemsComponent implements OnInit, AfterViewInit {
  @Input() isReporterView: boolean = false;

  previewUrl: string | null = null;
  selectedFile: File | null = null;

  displayedColumns: string[] = [
    'expandToggle', 'id', 'name', 'location', 'city',
    'description', 'lostDate', 'image', 'actions'
  ];
  dataSource = new MatTableDataSource<any>();
  isLoading = false;
  expandedElement: LostItem | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private lostItemService: LoserService) {}

  cities: string[] = [
    'Thrissur', 'Irinjalakuda', 'Chalakudy', 'Kodungallur',
    'Kunnamkulam', 'Wadakkanchery', 'Guruvayur', 'Anthikad',
    'Puthukkad', 'Mala'
  ];

  ngOnInit(): void {
    this.loadLostItems();

    this.dataSource.filterPredicate = (data: any, filter: string) => {
      if (data.detailRow) return false;
      const dataStr = `${data.name} ${data.location} ${data.description} ${data.lostDate}`;
      return dataStr.toLowerCase().includes(filter);
    };
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadLostItems(): void {
    this.isLoading = true;

    const fetch$ = this.isReporterView
      ? this.lostItemService.getAllLostItemsForReporter()
      : this.lostItemService.getMyLostItems();

    fetch$.subscribe({
      next: (data: LostItem[]) => {
        const withImageUrls = data.map((item) => ({
          ...item,
          imageUrl: item.imagePath ? `http://localhost:8080${item.imagePath}` : null
        }));
        const rowsWithExpand = withImageUrls
          .map(item => [item, { detailRow: true, ...item }])
          .flat();
        this.dataSource.data = rowsWithExpand;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching lost items:', err);
        this.isLoading = false;
      }
    });
  }

  deleteItem(id: number): void {
    const confirmed = confirm('Are you sure you want to delete this item?');
    if (confirmed) {
      this.lostItemService.deleteLostItem(id).subscribe({
        next: () => this.loadLostItems(),
        error: err => console.error('Error deleting item:', err)
      });
    }
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    this.dataSource.filter = filterValue;
  }

  isExpansionDetailRow = (index: number, row: any): boolean =>
    row.hasOwnProperty('detailRow');
}
