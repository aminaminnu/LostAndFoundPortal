import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

// 🔹 Model Interface for Found Item
export interface FoundItem {
  id?: number;
  name: string;
  description: string;
  location: string;
  city?: string;
  foundDate: string;           // ISO format string (e.g. "2025-06-24")
  imagePath?: string;          // Relative path from backend (e.g. "/uploads/found-images/...")
  imageUrl?: string | null;    // Full URL used in frontend (computed)
  userId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class FoundItemService {
 
  private apiUrl = 'http://localhost:8080/found-items';

  constructor(private http: HttpClient) {}

  // 🔐 Helper to attach JWT token from local storage
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken') || '';
    return new HttpHeaders({
      Authorization: 'Bearer ' + token
    });
  }

  
  // ✅ GET only logged-in user's items
getMyFoundItems(): Observable<FoundItem[]> {
  return this.http.get<FoundItem[]>(`${this.apiUrl}/my-items`, {
    headers: this.getAuthHeaders()
  });
}

  // 📥 GET specific item by ID
  getFoundItem(id: number): Observable<FoundItem> {
    return this.http.get<FoundItem>(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }
   getSuggestedItems(): Observable<FoundItem[]> {
  return this.http.get<FoundItem[]>(`${this.apiUrl}/suggested`, {
    headers: this.getAuthHeaders()
  });
}


  // ➕ POST: Add item with image using FormData
  addFoundItemWithFormData(formData: FormData): Observable<FoundItem> {
    return this.http.post<FoundItem>(`${this.apiUrl}/upload`, formData, {
      headers: this.getAuthHeaders().delete('Content-Type') // Required for multipart/form-data
    });
  }

  // ✏️ PUT: Update item with image using FormData
  updateFoundItemWithFormData(id: number, formData: FormData): Observable<FoundItem> {
    return this.http.put<FoundItem>(`${this.apiUrl}/upload/${id}`, formData, {
      headers: this.getAuthHeaders().delete('Content-Type')
    });
  }

  // ❌ DELETE item
  deleteFoundItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

 getAllFoundItemsForReporter(): Observable<FoundItem[]> {
  return this.http.get<FoundItem[]>(`${this.apiUrl}/all`, {
    headers: this.getAuthHeaders()
  });
}


}
