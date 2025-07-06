import {Component, inject, OnInit} from '@angular/core';
import {SeoService} from '../../core/services/essentials/seo.service';
import {AboutModel} from '../../core/models/public/about.model';
import {DataService} from '../../core/services/public/data.service';

@Component({
    selector: 'app-about',
    imports: [],
    templateUrl: './about.html',
    styleUrl: './about.css'
})
export class About implements OnInit {
    private readonly seoService = inject(SeoService);
    private readonly dataService = inject(DataService);
    about: AboutModel = {
        id: 1,
        imageUrl: 'https://images.pexels.com/photos/1065084/pexels-photo-1065084.jpeg?auto=compress&cs=tinysrgb&w=150',
        story: `Die Stadt- & Feuerwehrkapelle Eisenstadt blickt auf eine mehr als 40-jährige Geschichte zurück. Im Jahr 1985 wurde der Grundstein gelegt, als eine Gruppe leidenschaftlicher Musiker zusammenkam, um ihre gemeinsame Liebe zur Blasmusik in die Gemeinschaft zu tragen. Aus einem kleinen Ensemble ist über die Jahre eine der angesehensten Blaskapellen der Region gewachsen.

Unsere Reise ist geprägt von unzähligen Auftritten bei verschiedensten Anlässen – von intimen Gemeindeveranstaltungen bis hin zu großen Festen und Wettbewerben. Unser Repertoire ist breit gefächert und reicht von traditionellen Blasmusikklassikern über zeitgenössische Arrangements bis hin zu Eigenkompositionen, die die Vielseitigkeit und Ausdruckskraft unserer Blasinstrumente eindrucksvoll unter Beweis stellen.

Heute zählt die Stadt- & Feuerwehrkapelle Eisenstadt 25 engagierte Musikerinnen und Musiker. Wir treffen uns regelmäßig zu Proben, geben Konzerte und teilen unsere musikalische Leidenschaft mit einem Publikum jeden Alters. Wir sind stolz darauf, ein fester und aktiver Bestandteil der kulturellen Landschaft unserer Gemeinschaft zu sein.`,
        mission: [
            {
                title: 'Musikalische Exzellenz',
                description: 'Wir streben danach, die höchsten musikalischen Standards zu halten und gleichzeitig Blasmusik für alle zugänglich und erlebbar zu machen.'
            },
            {
                title: 'Engagement für die Gemeinschaft',
                description: 'Wir glauben an die Kraft der Musik, Menschen zusammenzubringen und unsere Gemeinschaft durch regelmäßige Auftritte und Bildungsinitiativen zu bereichern.'
            },
            {
                title: 'Jugendförderung',
                description: 'Wir engagieren uns leidenschaftlich für die Förderung der nächsten Musikergeneration durch unser Jugendprogramm und Mentoring-Möglichkeiten.'
            },
            {
                title: 'Musikalische Innovation',
                description: 'Während wir die Tradition ehren, begrüßen wir neue Musikstile und Arrangements, um unsere Aufführungen frisch und ansprechend zu halten.'
            }
        ]
    };

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Über den Verein',
            description: 'Finden Sie heraus, was der Verein in der Geschichte beigetragen hat und was unsere Mission ist.',
            keywords: 'blasmusik, eisenstadt, musikverein, feuerwehrkapelle, stadtkapelle, konzerte, burgenland, musik, termine'
        });

        this.loadAbout();
    }

    private loadAbout() {
        this.dataService.loadAbout().subscribe(about => {
            this.about = about.result;
        });
    }
}
