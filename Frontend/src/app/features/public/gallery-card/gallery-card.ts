import {ChangeDetectionStrategy, Component, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DataService} from '../../../core/services/data.service';
import {ImageModel} from '../../../core/models/image.model';
import {Loading} from '../../../shared/loading/loading';
import {ErrorMessage} from '../../../shared/error-message/error-message';
import {DatePipe, NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-gallery-card',
  standalone: true,
    imports: [
        Loading,
        ErrorMessage,
        NgOptimizedImage,
        DatePipe
    ],
  templateUrl: './gallery-card.html',
  styleUrl: './gallery-card.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GalleryCard implements OnInit {

    private readonly router = inject(Router);
    private readonly route = inject(ActivatedRoute);
    private readonly dataService = inject(DataService);

    //-------------------------------| Gallery |-------------------------------
    images = signal<ImageModel[]>([]);
    imageSectionLoading = signal<boolean>(true);
    imageSectionError = signal<boolean>(false);
    imageSectionErrorMessage = signal<string>("");
    //-------------------------------| Gallery |-------------------------------


    ngOnInit() {
        this.loadGalleryFromTitle();
    }

    loadGalleryFromTitle() {
        const slug = this.route.snapshot.paramMap.get('slug');
        if (!slug) {
            this.imageSectionError.set(true);
            this.imageSectionLoading.set(false);
            this.imageSectionErrorMessage.set('Gallery title not found');
            return;
        }

        this.dataService.loadGalleryImagesBySlug(slug).subscribe({
            next: (response) => {
                this.images.set(response.data);
                this.imageSectionLoading.set(false);
                this.imageSectionError.set(false);
            },
            error: error => {
                this.imageSectionError.set(true);
                this.imageSectionLoading.set(false);
                this.imageSectionErrorMessage.set(error.message ?? 'Unbekannter Fehler');
            }
        })
    }

    /**
     * Navigates back to the gallery overview page
     */
    goBack() {
        this.router.navigate(['/gallery']);
    }
}
