import {Component, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {SeoService} from '../../../core/services/seo.service';
import {Member} from '../../../core/models/members/Member.model';
import {NgClass, TitleCasePipe} from '@angular/common';

@Component({
  selector: 'app-member-detail',
    imports: [
        NgClass,
        TitleCasePipe
    ],
  templateUrl: './member-detail.html',
  styleUrl: './member-detail.css'
})
export class MemberDetail implements OnInit {
    private route = inject(ActivatedRoute);
    private router = inject(Router);
    private seoService = inject(SeoService);

    member = signal<Member | null>(null);

    // Mock data - would typically come from a service
    private members: Member[] = [
        {
            id: '1',
            name: 'Sarah Johnson',
            email: 'sarah.johnson@email.com',
            phone: '+1 234-567-8901',
            instrument: 'Bb Cornet',
            section: 'Cornet',
            joinDate: '2020-01-15',
            status: 'active',
            avatarUrl: 'https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?auto=compress&cs=tinysrgb&w=400',
            address: '123 Music Lane, Harmony City, HC 12345',
            emergencyContact: 'John Johnson (Husband)',
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
            avatarUrl: 'https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?auto=compress&cs=tinysrgb&w=400',
            address: '456 Brass Boulevard, Music Town, MT 54321',
            emergencyContact: 'Lisa Chen (Wife)',
            emergencyPhone: '+1 234-567-8904'
        }
    ];

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            const foundMember = this.members.find(m => m.id === id);
            this.member.set(foundMember || null);

            if (foundMember) {
                this.seoService.updateMetaTags({
                    title: `${foundMember.name} - Member Details - Blasmusik`,
                    description: `View details for ${foundMember.name}, ${foundMember.instrument} player in the brass band.`,
                    keywords: `member, ${foundMember.name}, ${foundMember.instrument}, brass band`
                });
            }
        }
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
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }
}
