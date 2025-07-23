import {ChangeDetectionStrategy, Component, inject, OnInit, signal} from '@angular/core';
import {DataService} from '../../../core/services/data.service';
import { GalleryModel } from '../../../core/models/gallery.model';
import {ErrorMessage} from '../../../shared/error-message/error-message';
import {Loading} from '../../../shared/loading/loading';
import {Router} from '@angular/router';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-gallery',
  standalone: true,
    imports: [
        ErrorMessage,
        Loading,
        DatePipe
    ],
  templateUrl: './gallery.html',
  styleUrl: './gallery.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class Gallery implements OnInit {

    private readonly dataService = inject(DataService);
    private readonly router = inject(Router);

    //-------------------------------| Gallery |-------------------------------
    gallery = signal<GalleryModel[]>([]);
    gallerySectionLoading = signal<boolean>(true);
    gallerySectionError = signal<boolean>(false);
    gallerySectionErrorMessage = signal<string>("");
    //-------------------------------| Gallery |-------------------------------

    ngOnInit() {
        this.loadGallery();
    }

    loadGallery() {
        this.dataService.loadGallery().subscribe({
            next: (response) => {
                this.gallery.set(response.data);
                this.gallerySectionLoading.set(false);
            },
            error: error => {
                this.gallery.set([]);
                this.gallerySectionLoading.set(false);
                this.gallerySectionError.set(true);
                this.gallerySectionErrorMessage.set(error.message ?? 'Unbekannter Fehler');
            }
        });
    }


    openGallery(imageTitle: string) {
        console.log('Opening Gallery' + imageTitle);
        this.router.navigate(['/gallery', imageTitle]);
    }
}
