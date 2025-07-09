import {inject, Injectable} from '@angular/core';
import {environment} from '../../../../environment/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiResponse} from '../../models/essentials/apiResponse.model';
import {GigModel} from '../../models/private/gig.model';
import {Message} from '../../models/private/message.model';
import {AnnouncementsModel} from '../../models/public/announcements.model';

@Injectable({
    providedIn: 'root'
})
export class DataService {

    baseUrl = environment.apiUrl;

    http: HttpClient = inject(HttpClient);

    loadGigs(): Observable<GigModel[]> {
        return this.http.get<GigModel[]>(this.baseUrl + '/public/gigs');
    }

    loadMessages(): Observable<ApiResponse<Message[]>> {
        return this.http.get<ApiResponse<Message[]>>(this.baseUrl + '/private/messages');
    }

    loadAnnouncements(): Observable<AnnouncementsModel[]> {
        return this.http.get<AnnouncementsModel[]>(this.baseUrl + '/public/announcements');
    }

    getGigById(gigId: string): Observable<GigModel> {
        return this.http.get<GigModel>(`${this.baseUrl}/public/gigs/${gigId}`);
    }

    updateGigAttendance(gigId: string, status: 'confirmed' | 'declined'): Observable<GigModel> {
        return this.http.put<GigModel>(`${this.baseUrl}/private/gigs/${gigId}/attendance`, {status});
    }
}
