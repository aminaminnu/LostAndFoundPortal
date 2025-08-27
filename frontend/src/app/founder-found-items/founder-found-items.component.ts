import { Component, OnInit, ViewChild, AfterViewInit, Input } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { FoundItem, FoundItemService } from '../services/found-item.service';

@Component({
  selector: 'app-founder-found-items',
  templateUrl: './founder-found-items.component.html',
  styleUrls: ['./founder-found-items.component.css']
})
export class FounderFoundItemsComponent implements OnInit, AfterViewInit {
  @Input() isReporterView: boolean = false;

  previewUrl: string | null = null;
  selectedFile: File | null = null;
  filterValue: string = '';
  selectedCity: string = '';
  foundItem: any = {};

  displayedColumns: string[] = [
    'expandToggle', 'name', 'location', 'city',
    'description', 'foundDate', 'image', 'actions'
  ];
  dataSource = new MatTableDataSource<any>();
  isLoading = false;
  expandedElement: FoundItem | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private foundItemService: FoundItemService) {}

  cities: string[] = [
    'Thrissur', 'Irinjalakuda', 'Chalakudy', 'Kodungallur',
    'Kunnamkulam', 'Wadakkanchery', 'Guruvayur', 'Anthikad',
    'Puthukkad', 'Mala'
  ];

  ngOnInit(): void {
    this.loadFoundItems();

    this.dataSource.filterPredicate = (data: any, filter: string) => {
      if (data.detailRow) return false;
      const searchTerms = JSON.parse(filter);
      const matchesCity = (data.city || '').toLowerCase().includes(searchTerms.city);
      const matchesText =
        (data.name || '').toLowerCase().includes(searchTerms.text) ||
        (data.location || '').toLowerCase().includes(searchTerms.text) ||
        (data.description || '').toLowerCase().includes(searchTerms.text);

      return matchesCity && matchesText;
    };
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.dataSource.sortingDataAccessor = (item, property) => {
      return item.detailRow ? '' : item[property];
    };

    this.applyCombinedFilter();
  }

  clearFilters(): void {
    this.filterValue = '';
    this.selectedCity = '';
    this.applyCombinedFilter();
  }

  loadFoundItems(): void {
  this.isLoading = true;

  const fetch$ = this.isReporterView
    ? this.foundItemService.getAllFoundItemsForReporter() // Reporter sees ALL found items
    : this.foundItemService.getMyFoundItems();            // Finder sees only their items

  fetch$.subscribe(
    data => {
      const withImageUrls = data.map(item => ({
        ...item,
        city: item.city,
        imageUrl: item.imagePath ? `http://localhost:8080${item.imagePath}` : null
      }));
      const rowsWithExpand = withImageUrls
        .map(item => [item, { detailRow: true, ...item }])
        .flat();
      this.dataSource.data = rowsWithExpand;
      this.isLoading = false;
    },
    error => {
      console.error('❌ Error fetching found items:', error);
      this.isLoading = false;
    }
  );
}

  deleteItem(id: number): void {
    const confirmed = confirm('Are you sure you want to delete this item?');
    if (confirmed) {
      this.foundItemService.deleteFoundItem(id).subscribe(() => {
        this.loadFoundItems();
      });
    }
  }

  applyFilter(event: Event): void {
    this.filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    this.applyCombinedFilter();
  }

  applyCombinedFilter(): void {
    const combinedFilter = {
      city: this.selectedCity || '',
      text: this.filterValue || '',
    };
    this.dataSource.filter = JSON.stringify(combinedFilter);
  }

  applyCityFilter(city: string): void {
    this.selectedCity = city.trim().toLowerCase();
    this.applyCombinedFilter();
  }

  isExpansionDetailRow = (index: number, row: any) =>
    row.hasOwnProperty('detailRow');
}
