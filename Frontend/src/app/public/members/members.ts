import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';
import {SeoService} from '../../core/services/essentials/seo.service';
import {MemberModel} from '../../core/models/public/member.model';
import {DatePipe} from '@angular/common';
import {DataService} from '../../core/services/public/data.service';

@Component({
    selector: 'app-members',
    standalone: true,
    imports: [FormsModule, RouterLink, DatePipe],
    templateUrl: './members.html',
    styleUrl: './members.css'
})
export class Members implements OnInit {
    private readonly seoService = inject(SeoService);
    private readonly dataService = inject(DataService);

    searchTerm = '';
    sectionFilter = '';
    statusFilter = '';

    currentPage = signal(1);
    pageSize = 10;

    protected Math = Math;
    members: MemberModel[] = [];

    filteredMembers = signal<MemberModel[]>([]);
    paginatedMembers = signal<MemberModel[]>([]);
    totalPages = signal(1);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Mitglieder - Blasmusik',
            description: 'Suchen Sie Mitglieder der Stadt- und Feuerwehrkapelle Eisenstadt',
            keywords: 'mitglieder, blasmusik, eisenstadt, musikverein, feuerwehrkapelle, stadtkapelle, konzerte, burgenland, musik, termine'
        });

        this.loadMembers();

        this.filteredMembers.set(this.members);
        this.updatePagination();
    }

    private loadMembers() {
        this.dataService.loadMembers().subscribe({
            next: (response) => {
                // Handle both direct array response and response with result property
                let members = Array.isArray(response) ? response : (response?.result || []);
                
                // Füge Standardbilder hinzu, falls keine vorhanden sind
                this.members = members.map(member => ({
                    ...member,
                    avatarUrl: member.avatarUrl || this.getDefaultAvatarUrl(member.name)
                }));
                
                console.log('Geladene Mitglieder:', this.members); // Debug-Ausgabe
                this.filteredMembers.set([...this.members]);
                this.updatePagination();
            },
            error: (error) => {
                console.error('Fehler beim Laden der Mitglieder:', error);
                this.members = [];
                this.filteredMembers.set([]);
                this.updatePagination();
            }
        });
    }

    private getDefaultAvatarUrl(name: string): string {
        // Erstelle eine konsistente Farbe basierend auf dem Namen
        const colors = [
            'FF5733', '33FF57', '3357FF', 'F3FF33', 'FF33F3',
            '33FFF3', 'FF8C33', '8C33FF', '33FF8C', 'FF338C'
        ];
        const charCode = name.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
        const color = colors[charCode % colors.length];
        
        return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=${color}&color=fff&size=256`;
    }

    protected filterMembers(): void {
        let filtered = this.members;

        // Filter by search term
        if (this.searchTerm.trim()) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(member =>
                member.name.toLowerCase().includes(term) ||
                member.instrument.toLowerCase().includes(term)
            );
        }

        // Filter by section
        if (this.sectionFilter) {
            filtered = filtered.filter(member => member.section === this.sectionFilter);
        }

        this.filteredMembers.set(filtered);
        this.currentPage.set(1);
        this.updatePagination();
    }

    private updatePagination(): void {
        const totalItems = this.filteredMembers().length;
        const totalPages = Math.ceil(totalItems / this.pageSize);
        this.totalPages.set(totalPages);

        const startIndex = (this.currentPage() - 1) * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        this.paginatedMembers.set(this.filteredMembers().slice(startIndex, endIndex));
    }

    protected previousPage(): void {
        if (this.currentPage() > 1) {
            this.currentPage.update(page => page - 1);
            this.updatePagination();
        }
    }

    protected nextPage(): void {
        if (this.currentPage() < this.totalPages()) {
            this.currentPage.update(page => page + 1);
            this.updatePagination();
        }
    }

    protected goToPage(page: number): void {
        this.currentPage.set(page);
        this.updatePagination();
    }

    protected getPageNumbers(): number[] {
        const total = this.totalPages();
        const current = this.currentPage();
        const delta = 2;
        const range = [];
        const rangeWithDots = [];

        for (let i = Math.max(2, current - delta); i <= Math.min(total - 1, current + delta); i++) {
            range.push(i);
        }

        if (current - delta > 2) {
            rangeWithDots.push(1, 0);
        } else {
            rangeWithDots.push(1);
        }

        rangeWithDots.push(...range);

        if (current + delta < total - 1) {
            rangeWithDots.push(0, total);
        } else {
            rangeWithDots.push(total);
        }

        return rangeWithDots.filter(page => page !== 0);
    }
}
