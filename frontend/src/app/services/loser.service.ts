import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LostItem {
  imagepath: any;
  image_path: any;
  id?: number;
  name: string;
  description: string;
  location: string;
  city?: string;
  lostDate: string;  // use ISO string format
  userId?: number;
  imagePath?: string;
  
}

@Injectable({
  providedIn: 'root'
})
export class LoserService {
  

  private apiUrl = 'http://localhost:8080/lost-items';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken') || '';
    return new HttpHeaders({
      'Authorization': 'Bearer ' + token
    });
  }

  getMyLostItems(): Observable<LostItem[]> {
  return this.http.get<LostItem[]>(`${this.apiUrl}/my-items`, {
    headers: this.getAuthHeaders()
  });
}

  getLostItem(id: number): Observable<LostItem> {
    return this.http.get<LostItem>(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // ✅ FIXED: Use /upload endpoint for POST with FormData
  addLostItem(formData: FormData): Observable<LostItem> {
    return this.http.post<LostItem>(`${this.apiUrl}/upload`, formData, {
      headers: this.getAuthHeaders().delete('Content-Type')
    });
  }

  // ✅ FIXED: Use /upload/{id} endpoint for PUT with FormData
  updateLostItem(id: number, formData: FormData): Observable<LostItem> {
    return this.http.put<LostItem>(`${this.apiUrl}/upload/${id}`, formData, {
     headers: this.getAuthHeaders().delete('Content-Type')

    });
  }




  deleteLostItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  getAllLostItemsForReporter(): Observable<LostItem[]> {
  return this.http.get<LostItem[]>(`${this.apiUrl}/all`);
}

}
