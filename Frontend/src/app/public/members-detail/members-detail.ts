import {Component, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SeoService} from '../../core/services/essentials/seo.service';
import {MemberModel} from '../../core/models/public/member.model';
import {DatePipe} from '@angular/common';

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

    member = signal<MemberModel | null>(null);
    memberRemarksText = signal<string>('');

    protected members: MemberModel[] = [
        {
            id: 1,
            name: 'Max Mustermann',
            instrument: 'Trompete',
            section: 'Hohes Blech',
            joinDate: '2020-01-15',
            avatarUrl: 'https://images.pexels.com/photos/1065084/pexels-photo-1065084.jpeg?auto=compress&cs=tinysrgb&w=150',
            remarks: [
                {
                    text: 'Teilnahme am Frühlingskonzert und den zugehörigen Proben. Hat sich aktiv eingebracht und gute Vorschläge zur Verbesserung der Ensemble-Performance gemacht. War pünktlich und zuverlässig bei allen Terminen. Eine sehr engagierte Leistung.',
                    timestamp: '2025-06-01',
                    type: 'accent'
                },
                {
                    text: 'Hervorragende Leistung beim Osterumzug, besonders im Solo-Part. Die Zuschauer waren begeistert von der Präzision und dem Ausdruck. Hat auch im Team gut funktioniert und andere unterstützt.',
                    timestamp: '2025-03-28',
                    type: 'primary'
                },
                {
                    text: 'Kontaktinformationen aktualisiert: Neue Telefonnummer und E-Mail-Adresse hinterlegt. Dies ist eine längere administrative Bemerkung, die keine musikalische Aktivität beschreibt, aber dennoch wichtig für die interne Kommunikation ist. Sie sollte vollständig sichtbar sein, auch wenn sie etwas länger ist.',
                    timestamp: '2025-02-10',
                    type: 'yellow'
                },
                {
                    text: 'Kürzere allgemeine Notiz zur Anwesenheit bei den letzten drei Übungseinheiten. Es gab keine besonderen Vorkommnisse, alles lief wie erwartet.',
                    timestamp: '2025-01-05',
                    type: 'accent'
                }
            ]
        },
        {
            id: 2,
            name: 'Anna Schmidt',
            instrument: 'Klarinette',
            section: 'Holz',
            joinDate: '2019-03-20',
            avatarUrl: 'https://images.pexels.com/photos/774909/pexels-photo-774909.jpeg?auto=compress&cs=tinysrgb&w=150',
            remarks: [
                {text: 'Anmeldung zum Sommerfest bestätigt.', timestamp: '2025-05-10', type: 'accent'},
                {
                    text: 'Neues Notenmaterial für das Herbstkonzert erhalten. Hat schnell die neuen Stücke gelernt und integriert.',
                    timestamp: '2025-04-20',
                    type: 'yellow'
                }
            ]
        }
    ];

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            const foundMember = this.members.find(member => member.id.toString() === id);
            this.member.set(foundMember || null);

            if (foundMember) {
                this.seoService.updateMetaTags({
                    title: `${foundMember.name} - Member Details - Blasmusik`,
                    description: `View details for ${foundMember.name}, ${foundMember.instrument} player in the brass band.`,
                    keywords: `member, ${foundMember.name}, ${foundMember.instrument}, brass band`
                });

                this.memberRemarksText.set(this.formatRemarksForDisplay(foundMember.remarks || []));
            }
        }
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
}
