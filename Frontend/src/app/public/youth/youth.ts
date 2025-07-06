import {Component, inject, OnInit, signal} from '@angular/core';
import {SeoService} from '../../core/services/essentials/seo.service';
import {MemberModel} from '../../core/models/public/member.model';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-youth',
    imports: [],
  templateUrl: './youth.html',
  styleUrl: './youth.css'
})
export class Youth implements OnInit { // Umbenennung zu YouthComponent für Klarheit
    private readonly seoService = inject(SeoService);

    // Signal für die Nachwuchsmitglieder
    youthMembers = signal<MemberModel[]>([]);

    // Mock-Daten für die Nachwuchsmitglieder ("Die Feuerspritzen")
    // In einer echten Anwendung würden diese von einem Service geladen
    private readonly mockYouthMembers: MemberModel[] = [
        {
            id: 101,
            name: 'Lena Bachmann',
            instrument: 'Flöte (Junior)',
            section: 'Holz',
            joinDate: '2023-09-01',
            avatarUrl: 'https://images.pexels.com/photos/3760263/pexels-photo-3760263.jpeg?auto=compress&cs=tinysrgb&w=150',
            remarks: [{text: 'Sehr fleißig und schnell lernend.', timestamp: '2024-06-01', type: 'accent'}]
        },
        {
            id: 102,
            name: 'Moritz Huber',
            instrument: 'Kleine Trommel',
            section: 'Schlagwerk',
            joinDate: '2023-10-15',
            avatarUrl: 'https://images.pexels.com/photos/2069796/pexels-photo-2069796.jpeg?auto=compress&cs=tinysrgb&w=150',
            remarks: [{text: 'Zeigt großes Talent im Rhythmusgefühl.', timestamp: '2024-05-20', type: 'primary'}]
        },
        {
            id: 103,
            name: 'Sophie Novak',
            instrument: 'Trompete (Anfänger)',
            section: 'Hohes Blech',
            joinDate: '2024-01-20',
            avatarUrl: 'https://images.pexels.com/photos/10497556/pexels-photo-10497556.jpeg?auto=compress&cs=tinysrgb&w=150',
            remarks: [{text: 'Muss noch üben, aber motiviert.', timestamp: '2024-06-10', type: 'yellow'}]
        },
        {
            id: 104,
            name: 'David Berger',
            instrument: 'Bariton (Junior)',
            section: 'Tiefes Blech',
            joinDate: '2023-11-01',
            avatarUrl: 'https://images.pexels.com/photos/2070335/pexels-photo-2070335.jpeg?auto=compress&cs=tinysrgb&w=150',
            remarks: [{
                text: 'Guter Teamplayer, schon bei ersten Auftritten dabei.',
                timestamp: '2024-06-15',
                type: 'accent'
            }]
        }
        // Füge hier weitere Nachwuchsmitglieder hinzu
    ];

    ngOnInit(): void {
        // SEO-Meta-Tags setzen
        this.seoService.updateMetaTags({
            title: 'Unsere Feuerspritzen - Nachwuchs der Blasmusik Eisenstadt',
            description: 'Entdecke die jungen Talente, die die Zukunft der Stadt- und Feuerwehrkapelle Eisenstadt gestalten.',
            keywords: 'nachwuchs, feuerspritzen, jugend, blasmusik, eisenstadt, musikverein, feuerwehrkapelle, talente'
        });

        // Mock-Daten laden (simuliert Service-Aufruf)
        this.youthMembers.set(this.mockYouthMembers);
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
