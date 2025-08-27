import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FoundItem, FoundItemService } from '../services/found-item.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-founder-found-item-form',
  templateUrl: './founder-found-item-form.component.html',
  styleUrls: ['./founder-found-item-form.component.css']
})
export class FounderFoundItemFormComponent implements OnInit {
foundItemForm!: FormGroup;
  id?: number;
  isEditMode = false;
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;
  previewUrl: any;
  isSubmitting: boolean | undefined;

  constructor(
    private fb: FormBuilder,
    private foundItemService: FoundItemService,
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

    this.foundItemForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      location: ['', Validators.required],
      foundDate: ['', Validators.required]
    });

    if (this.isEditMode) {
      this.foundItemService.getFoundItem(this.id!).subscribe(item => {
        this.foundItemForm.patchValue(item);
        if (item.imagePath) {
  this.previewUrl = `http://localhost:8080${item.imagePath}`;
}

      });
    }
  }

onFileSelected(event: any): void {
  const file = event.target.files[0];
  if (file) {
    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.previewUrl = e.target.result;
    };
    reader.readAsDataURL(file);
  }
}

 onSubmit(): void {
  if (this.foundItemForm.invalid) return;

  this.isSubmitting = true;

  const formData = new FormData();
  formData.append('name', this.foundItemForm.get('name')?.value);
  formData.append('description', this.foundItemForm.get('description')?.value);
  formData.append('location', this.foundItemForm.get('location')?.value);



  const rawDate = this.foundItemForm.get('foundDate')?.value;
  const formattedDate = new Date(rawDate).toISOString().split('T')[0];
  formData.append('foundDate', formattedDate);

  if (this.selectedFile) {
    formData.append('image', this.selectedFile);
  }

  const request = this.isEditMode
    ? this.foundItemService.updateFoundItemWithFormData(this.id!, formData)
    : this.foundItemService.addFoundItemWithFormData(formData);

  request.subscribe({
    next: () => this.router.navigate(['/founder/my-found-items']),
    error: err => {
      console.error("Upload failed", err);
      this.isSubmitting = false;
    }
  });
}

}
