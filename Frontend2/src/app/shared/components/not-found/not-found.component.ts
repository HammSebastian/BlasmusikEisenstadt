import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SeoService } from '../../../core/services/seo.service';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="min-h-screen bg-secondary-50 flex items-center justify-center px-4">
      <div class="max-w-md w-full text-center">
        <div class="mb-8">
          <h1 class="text-9xl font-bold text-primary-600">404</h1>
          <h2 class="text-2xl font-semibold text-secondary-900 mb-4">Page Not Found</h2>
          <p class="text-secondary-600 mb-8">
            The page you're looking for doesn't exist or has been moved.
          </p>
        </div>
        
        <div class="space-y-4">
          <a routerLink="/home" class="btn-primary block">
            Go to Dashboard
          </a>
          <button (click)="goBack()" class="btn-secondary block">
            Go Back
          </button>
        </div>
      </div>
    </div>
  `
})
export class NotFoundComponent implements OnInit {
  private seoService = inject(SeoService);

  ngOnInit(): void {
    this.seoService.updateMetaTags({
      title: 'Page Not Found - Blasmusik',
      description: 'The page you are looking for could not be found.',
    });
  }

  protected goBack(): void {
    window.history.back();
  }
}