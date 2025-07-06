import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgClass, TitleCasePipe} from '@angular/common';
import {SeoService} from '../../../core/services/seo.service';
import {RouterLink} from '@angular/router';
import {Member} from '../../../core/models/members/Member.model';

@Component({
  selector: 'app-member-list',
    imports: [
        FormsModule,
        TitleCasePipe,
        NgClass,
        RouterLink
    ],
  templateUrl: './member-list.html',
  styleUrl: './member-list.css'
})
export class MemberList implements OnInit {
    private seoService = inject(SeoService);

    searchTerm = '';
    sectionFilter = '';
    statusFilter = '';

    currentPage = signal(1);
    pageSize = 10;

    protected Math = Math;

    private members = signal<Member[]>([
        {
            id: '1',
            name: 'Sarah Johnson',
            email: 'sarah.johnson@email.com',
            phone: '+1 234-567-8901',
            instrument: 'Bb Cornet',
            section: 'Cornet',
            joinDate: '2020-01-15',
            status: 'active',
            avatarUrl: 'https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&w=150',
            address: '123 Music Lane, Harmony City',
            emergencyContact: 'John Johnson',
            emergencyPhone: '+1 234-567-8902'
        },
        {
            id: '2',
            name: 'Michael Chen',
            email: 'michael.chen@email.com',
            phone: '+1 234-567-8903',
            instrument: 'French Horn',
            section: 'Horn',
            joinDate: '2019-03-20',
            status: 'active',
            avatarUrl: 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?auto=compress&cs=tinysrgb&w=150',
            address: '456 Brass Boulevard, Music Town',
            emergencyContact: 'Lisa Chen',
            emergencyPhone: '+1 234-567-8904'
        },
        {
            id: '3',
            name: 'Emily Rodriguez',
            email: 'emily.rodriguez@email.com',
            phone: '+1 234-567-8905',
            instrument: 'Tenor Trombone',
            section: 'Trombone',
            joinDate: '2021-09-10',
            status: 'active',
            avatarUrl: 'https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg?auto=compress&cs=tinysrgb&w=150',
            address: '789 Symphony Street, Melody City',
            emergencyContact: 'Carlos Rodriguez',
            emergencyPhone: '+1 234-567-8906'
        },
        {
            id: '4',
            name: 'David Thompson',
            email: 'david.thompson@email.com',
            phone: '+1 234-567-8907',
            instrument: 'Euphonium',
            section: 'Euphonium',
            joinDate: '2018-06-05',
            status: 'active',
            avatarUrl: 'https://images.pexels.com/photos/1851164/pexels-photo-1851164.jpeg?auto=compress&cs=tinysrgb&w=150',
            address: '321 Harmony Avenue, Brass City',
            emergencyContact: 'Jennifer Thompson',
            emergencyPhone: '+1 234-567-8908'
        },
        {
            id: '5',
            name: 'Lisa Martinez',
            email: 'lisa.martinez@email.com',
            phone: '+1 234-567-8909',
            instrument: 'Eb Tuba',
            section: 'Tuba',
            joinDate: '2022-02-14',
            status: 'active',
            avatarUrl: 'https://images.pexels.com/photos/1819483/pexels-photo-1819483.jpeg?auto=compress&cs=tinysrgb&w=150',
            address: '654 Bass Street, Low Note Lane',
            emergencyContact: 'Antonio Martinez',
            emergencyPhone: '+1 234-567-8910'
        },
        {
            id: '6',
            name: 'James Wilson',
            email: 'james.wilson@email.com',
            phone: '+1 234-567-8911',
            instrument: 'Timpani',
            section: 'Percussion',
            joinDate: '2017-11-30',
            status: 'on-leave',
            avatarUrl: 'https://images.pexels.com/photos/1043471/pexels-photo-1043471.jpeg?auto=compress&cs=tinysrgb&w=150',
            address: '987 Rhythm Road, Beat City',
            emergencyContact: 'Maria Wilson',
            emergencyPhone: '+1 234-567-8912'
        }
    ]);

    filteredMembers = signal<Member[]>([]);
    paginatedMembers = signal<Member[]>([]);
    totalPages = signal(1);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Members - Blasmusik',
            description: 'Browse and manage brass band members, their instruments, and contact information.',
            keywords: 'members, brass band, musicians, instruments, contact'
        });

        this.filteredMembers.set(this.members());
        this.updatePagination();
    }

    protected filterMembers(): void {
        let filtered = this.members();

        // Filter by search term
        if (this.searchTerm.trim()) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(member =>
                member.name.toLowerCase().includes(term) ||
                member.email.toLowerCase().includes(term) ||
                member.instrument.toLowerCase().includes(term)
            );
        }

        // Filter by section
        if (this.sectionFilter) {
            filtered = filtered.filter(member => member.section === this.sectionFilter);
        }

        // Filter by status
        if (this.statusFilter) {
            filtered = filtered.filter(member => member.status === this.statusFilter);
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

    protected getStatusClasses(status: string): string {
        switch (status) {
            case 'active':
                return 'bg-accent-100 text-accent-800';
            case 'inactive':
                return 'bg-red-100 text-red-800';
            case 'on-leave':
                return 'bg-yellow-100 text-yellow-800';
            default:
                return 'bg-secondary-100 text-secondary-800';
        }
    }

    protected formatDate(dateString: string): string {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    }
}
