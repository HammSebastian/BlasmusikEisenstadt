import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NewsArticle, CreateNewsArticleRequest, UpdateNewsArticleRequest, Page } from '../models/news.model';
import { AuthService } from './auth.service'; // To get auth headers

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  private apiUrl = 'http://localhost:8080/api/news'; // Adjust if backend URL is different

  constructor(private http: HttpClient, private authService: AuthService) { }

  // --- Public Access Methods ---

  // Get all published news articles (paginated)
  getPublishedNews(page: number, size: number, sort: string = 'publicationDate,desc'): Observable<Page<NewsArticle>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<NewsArticle>>(`${this.apiUrl}`, { params });
  }

  // Get a single news article by ID (publicly accessible if published)
  getNewsArticleById(id: number): Observable<NewsArticle> {
    // The backend controller handles logic for whether an unpublished article can be viewed
    // (e.g. if user is admin/editor). For a public call, we just attempt to get it.
    // If auth is needed for unpublished, the authService.getAuthHeaders() would be used by an admin version of this.
    // For now, assuming this is for potentially public view, no special headers beyond what interceptors might add.
    // If we strictly want to use this method from an admin context where unpublished can be fetched,
    // we might need to pass auth headers.
    // The controller logic getNewsArticleById checks if user is admin/editor to show unpublished.
    // So, if an admin is logged in, their token will be sent by an interceptor (if configured)
    // or we add it manually. For simplicity, assuming an interceptor handles token for all requests.
     const headers = this.authService.getAuthHeaders(); // Send token if available, backend decides access
    return this.http.get<NewsArticle>(`${this.apiUrl}/${id}`, { headers });
  }


  // --- Admin/Editor Methods (require authentication) ---

  // Get all news articles - published or not (for admin/editor, paginated)
  getAllNewsArticles(page: number, size: number, sort: string = 'publicationDate,desc'): Observable<Page<NewsArticle>> {
    const headers = this.authService.getAuthHeaders();
    if (!headers.has('Authorization')) {
        // This case should ideally not happen if called from an admin context
        // Or be handled by an AuthGuard before calling the service.
        // Consider throwing an error or returning an empty observable.
        console.error("Attempted to call admin API without auth token.");
        // return throwError(() => new Error("Not authenticated")); // Example
    }
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Page<NewsArticle>>(`${this.apiUrl}/all`, { headers, params });
  }

  // Create a new news article
  createNewsArticle(articleData: CreateNewsArticleRequest): Observable<NewsArticle> {
    const headers = this.authService.getAuthHeaders();
    return this.http.post<NewsArticle>(this.apiUrl, articleData, { headers });
  }

  // Update an existing news article
  updateNewsArticle(id: number, articleData: UpdateNewsArticleRequest): Observable<NewsArticle> {
    const headers = this.authService.getAuthHeaders();
    return this.http.put<NewsArticle>(`${this.apiUrl}/${id}`, articleData, { headers });
  }

  // Delete a news article
  deleteNewsArticle(id: number): Observable<any> { // Response is typically just a confirmation string or 200 OK
    const headers = this.authService.getAuthHeaders();
    return this.http.delete(`${this.apiUrl}/${id}`, { headers, responseType: 'text' });
  }
}
