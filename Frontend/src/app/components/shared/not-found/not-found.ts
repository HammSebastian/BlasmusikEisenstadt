import {Component, inject} from '@angular/core';
import {SeoService} from '../../../core/services/seo';

@Component({
  selector: 'app-not-found',
  imports: [],
  templateUrl: './not-found.html',
  styleUrl: './not-found.css'
})
export class NotFound {
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
