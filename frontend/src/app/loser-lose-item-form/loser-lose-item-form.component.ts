import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LostItem, LoserService } from '../services/loser.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-lost-item-form',
  templateUrl: './loser-lose-item-form.component.html',
  styleUrls: ['./loser-lose-item-form.component.css']
})
export class LoserLoseItemFormComponent implements OnInit {
  
  lostItemForm!: FormGroup;
  id?: number;
  isEditMode = false;
  selectedFile: File | null = null;
  previewUrl: string | ArrayBuffer | null = null;
  isLoading: boolean = false;


  lostItem: any = {}; // ✅ Add this line to fix the error

  constructor(
    private fb: FormBuilder,
    private lostItemService: LoserService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
cities: string[] = [
  'Thrissur',
  'Irinjalakuda',
  'Chalakudy',
  'Kodungallur',
  'Kunnamkulam',
  'Wadakkanchery',
  'Guruvayur',
  'Anthikad',
  'Puthukkad',
  'Mala'
];

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.isEditMode = !!this.id;

    this.lostItemForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      location: ['', Validators.required],
      lostDate: ['', Validators.required],
      
    });

   if (this.isEditMode) {
  this.lostItemService.getLostItem(this.id!).subscribe(item => {
    this.lostItem = item;
    this.lostItemForm.patchValue(item);

    // ⚠️ Do NOT set previewUrl here!
    // Let the HTML show lostItem.image_path by default
  });
}

  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onSubmit(): void {
  if (this.lostItemForm.invalid) return;

  const formData = new FormData();
  formData.append('name', this.lostItemForm.get('name')?.value);
  formData.append('description', this.lostItemForm.get('description')?.value);
  formData.append('location', this.lostItemForm.get('location')?.value);


  const rawDate = this.lostItemForm.get('lostDate')?.value;
  const formattedDate = new Date(rawDate).toISOString().split('T')[0];
  formData.append('lostDate', formattedDate);

  if (this.selectedFile) {
    formData.append('image', this.selectedFile);
  }

  if (this.isEditMode) {
    this.lostItemService.updateLostItem(this.id!, formData).subscribe(() => {
      this.router.navigate(['/lost/my-lost-items']);
    });
  } else {
    this.lostItemService.addLostItem(formData).subscribe(() => {
      this.router.navigate(['/lost/my-lost-items']);
    });
  }
}

}
