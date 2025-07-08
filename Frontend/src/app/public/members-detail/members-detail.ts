import {Component, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SeoService} from '../../core/services/essentials/seo.service';
import {MemberModel} from '../../core/models/public/member.model';
import {DatePipe} from '@angular/common';
import {DataService} from '../../core/services/public/data.service';

export interface Remark {
    text: string;
    timestamp: string;
    type?: 'accent' | 'primary' | 'yellow';
}

@Component({
    selector: 'app-members-detail',
    standalone: true,
    imports: [DatePipe],
    templateUrl: './members-detail.html',
    styleUrl: './members-detail.css'
})
export class MembersDetail implements OnInit {
    private readonly route = inject(ActivatedRoute);
    private readonly router = inject(Router);
    private readonly seoService = inject(SeoService);
    private readonly dataService = inject(DataService);

    member = signal<MemberModel | null>(null);
    memberRemarksText = signal<string>('');
    isLoading = signal<boolean>(true);
    error = signal<string | null>(null);

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.loadMember(id);
        } else {
            this.error.set('Keine Mitglieds-ID angegeben.');
            this.isLoading.set(false);
        }
    }

    private loadMember(id: string): void {
        this.isLoading.set(true);
        this.error.set(null);

        this.dataService.loadMemberById(id).subscribe({
            next: (response) => {
                const memberData = response?.result || response;
                if (memberData) {
                    this.member.set({
                        ...memberData,
                        // Stelle sicher, dass die avatarUrl gesetzt ist
                        avatarUrl: memberData.avatarUrl || this.getDefaultAvatarUrl(memberData.name)
                    });

                    this.seoService.updateMetaTags({
                        title: `${memberData.name} - Mitglied - Blasmusik Eisenstadt`,
                        description: `Profil von ${memberData.name}, ${memberData.instrument}`,
                        keywords: `mitglied, ${memberData.name}, ${memberData.instrument}, blasmusik, eisenstadt`
                    });

                    if (memberData.remarks && memberData.remarks.length > 0) {
                        this.memberRemarksText.set(this.formatRemarksForDisplay(memberData.remarks));
                    } else {
                        this.memberRemarksText.set('Keine Bemerkungen vorhanden.');
                    }
                } else {
                    this.error.set('Mitglied nicht gefunden.');
                }
                this.isLoading.set(false);
            },
            error: (error) => {
                console.error('Fehler beim Laden des Mitglieds:', error);
                this.error.set('Fehler beim Laden der Mitgliederdaten. Bitte versuchen Sie es später erneut.');
                this.isLoading.set(false);
            }
        });
    }

    protected getDefaultAvatarUrl(name: string): string {
        // Erstelle eine konsistente Farbe basierend auf dem Namen
        const colors = [
            'FF5733', '33FF57', '3357FF', 'F3FF33', 'FF33F3',
            '33FFF3', 'FF8C33', '8C33FF', '33FF8C', 'FF338C'
        ];
        const charCode = name.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0);
        const color = colors[charCode % colors.length];

        return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=${color}&color=fff&size=256`;
    }

    private formatRemarksForDisplay(remarks: Remark[]): string {
        if (!remarks || remarks.length === 0) {
            return 'Keine Bemerkungen vorhanden.';
        }
        return remarks.map(r => `[${r.timestamp}] ${r.text}`).join('\n\n');
    }

    protected goBack(): void {
        this.router.navigate(['/members']);
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
        return date.toLocaleDateString('de-DE', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }

    protected onImageError(event: Event): void {
        const imgElement = event.target as HTMLImageElement;
        if (this.member()?.name) {
            imgElement.src = this.getDefaultAvatarUrl(this.member()!.name);
        } else {
            imgElement.src = 'https://ui-avatars.com/api/?name=Member&background=random';
        }
    }
}
