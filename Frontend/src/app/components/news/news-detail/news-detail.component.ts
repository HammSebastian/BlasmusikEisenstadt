import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common'; // DatePipe for formatting dates
import { ActivatedRoute, RouterModule } from '@angular/router'; // ActivatedRoute to get route params, RouterModule for routerLink
import { NewsService } from '../../../services/news.service';
import { NewsArticle } from '../../../models/news.model';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser'; // For sanitizing HTML content if needed

@Component({
  selector: 'app-news-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, DatePipe], // Add DatePipe to providers if not already global
  templateUrl: './news-detail.component.html',
  styleUrls: ['./news-detail.component.scss']
})
export class NewsDetailComponent implements OnInit {
  article: NewsArticle | undefined;
  isLoading: boolean = true;
  errorMessage: string = '';
  articleContent: SafeHtml | undefined;


  constructor(
    private route: ActivatedRoute,
    private newsService: NewsService,
    private sanitizer: DomSanitizer // For rendering HTML content safely
  ) { }

  ngOnInit(): void {
    const articleId = this.route.snapshot.paramMap.get('id');
    if (articleId) {
      this.loadArticle(+articleId); // Convert string id to number
    } else {
      this.errorMessage = 'Article ID not found in route.';
      this.isLoading = false;
    }
  }

  loadArticle(id: number): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.newsService.getNewsArticleById(id).subscribe({
      next: (data) => {
        this.article = data;
        // If content is HTML and needs to be rendered as such:
        // Be VERY careful with this and ensure content is sanitized on the backend
        // or comes from a trusted source.
        // this.articleContent = this.sanitizer.bypassSecurityTrustHtml(this.article.content);
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load article. It may not exist or you may not have permission to view it.';
        if (err.status === 404) {
            this.errorMessage = 'The article you are looking for was not found.';
        } else if (err.status === 403) {
            this.errorMessage = 'You do not have permission to view this article, or it is not yet published.';
        }
        console.error(err);
        this.isLoading = false;
      }
    });
  }
}
