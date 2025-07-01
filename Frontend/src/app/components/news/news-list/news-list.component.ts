import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; // For routerLink
import { NewsService } from '../../../services/news.service';
import { NewsArticle, Page } from '../../../models/news.model';
// import { PaginatorModule } from 'primeng/paginator'; // Example if using a UI library for pagination

@Component({
  selector: 'app-news-list',
  standalone: true,
  imports: [CommonModule, RouterModule /*, PaginatorModule*/ ], // Add PaginatorModule if used
  templateUrl: './news-list.component.html',
  styleUrls: ['./news-list.component.scss']
})
export class NewsListComponent implements OnInit {
  newsPage: Page<NewsArticle> | undefined;
  isLoading: boolean = true;
  errorMessage: string = '';

  currentPage: number = 0; // 0-indexed for backend
  pageSize: number = 9; // 3 items per row, 3 rows

  constructor(private newsService: NewsService) { }

  ngOnInit(): void {
    this.loadNews();
  }

  loadNews(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.newsService.getPublishedNews(this.currentPage, this.pageSize).subscribe({
      next: (page) => {
        this.newsPage = page;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load news articles. Please try again later.';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  // Basic pagination handling (can be replaced with a UI component like PrimeNG Paginator)
  onPageChange(newPage: number): void {
    if (this.newsPage && newPage >= 0 && newPage < this.newsPage.totalPages) {
      this.currentPage = newPage;
      this.loadNews();
    }
  }

  get totalPagesArray(): number[] {
    return this.newsPage ? Array(this.newsPage.totalPages).fill(0).map((x, i) => i) : [];
  }

  // Helper to create a summary if one isn't provided or to truncate content
  getSummary(article: NewsArticle): string {
    if (article.summary && article.summary.trim().length > 0) {
      return article.summary;
    }
    // Create a summary from content if no explicit summary
    const words = article.content.split(' ');
    return words.slice(0, 30).join(' ') + (words.length > 30 ? '...' : '');
  }
}
