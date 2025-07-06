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
        /*
        this.dataService.loadMembers().subscribe(members => {
            this.members = members.result;
            this.filterMembers();
        });
        */

        // Dummy data
        this.members = [
            {
                id: 1,
                name: 'Max Mustermann',
                instrument: 'Glocke',
                section: 'Holz',
                avatarUrl: 'https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&w=150',
                joinDate: '2022-01-01',
            }
        ];

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
