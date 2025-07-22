import {Component, inject, OnInit, signal} from '@angular/core';
import {Loading} from '../../../shared/loading/loading';
import {ErrorMessage} from '../../../shared/error-message/error-message';
import {HistoryModel, Section} from '../../../core/models/history.model';
import {DataService} from '../../../core/services/data.service';

type SectionWithIndex = { index: number; section: Section };

@Component({
  selector: 'app-history',
    imports: [
        Loading,
        ErrorMessage
    ],
  templateUrl: './history.html',
  styleUrl: './history.css'
})
export class History implements OnInit {

    private readonly dataService = inject(DataService);

    history = signal<HistoryModel | null>(null);
    sectionsWithIndex = signal<SectionWithIndex[]>([]);
    loading = signal(true);
    error = signal(false);
    errorMessage = signal('');

    visibleItems = [] as number[];

    ngOnInit() {
        this.loadHistory();
        this.setupScrollListener();
    }

    loadHistory() {
        this.dataService.loadHistoryData().subscribe({
            next: (response) => {
                this.history.set(response.data);
                this.loading.set(false);
                const reversed = response.data.sections.reverse();
                this.sectionsWithIndex.set(reversed.map((section, i) => ({ index: i, section })));

                // Nach 2 Sekunden erstes Element sichtbar machen
                setTimeout(() => {
                    if (this.sectionsWithIndex().length > 0) {
                        this.visibleItems = [0];
                        // Optional: Scroll zum ersten Element wenn du möchtest
                        const firstEl = document.querySelector(`[data-index="0"]`);
                        if (firstEl) {
                            firstEl.scrollIntoView({ behavior: 'smooth', block: 'start' });
                        }
                    }
                }, 1500);
            },
            error: (error) => {
                console.error('Error loading history:', error);
                this.history.set(null);
                this.error.set(true);
                this.loading.set(false);
                this.errorMessage.set(error.message ?? 'Unbekannter Fehler');
            }
        });
    }

    setupScrollListener() {
        window.addEventListener('scroll', () => this.checkVisibleItems());
    }

    checkVisibleItems() {
        const items = document.querySelectorAll('[data-index]');
        const windowHeight = window.innerHeight;
        const newVisible: number[] = [];

        items.forEach((el) => {
            const rect = el.getBoundingClientRect();
            if (rect.top < windowHeight - 100) {
                const idx = Number(el.getAttribute('data-index'));
                if (!this.visibleItems.includes(idx)) {
                    newVisible.push(idx);
                }
            }
        });

        if (newVisible.length > 0) {
            this.visibleItems = Array.from(new Set([...this.visibleItems, ...newVisible]));
        }
    }
}
