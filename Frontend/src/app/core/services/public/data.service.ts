import {inject, Injectable} from '@angular/core';
import {environment} from '../../../../environment/environment';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';
import {HeroItemModel} from '../../models/public/heroItem.model';
import {AnnouncementsModel} from '../../models/public/announcements.model';
import {ApiResponse} from '../../models/essentials/apiResponse.model';
import {GigModel} from '../../models/public/gig.model';
import { MemberModel } from '../../models/public/member.model';
import {AboutModel} from '../../models/public/about.model';

@Injectable({
    providedIn: 'root'
})
export class DataService {

    baseUrl = environment.apiUrl;

    http: HttpClient = inject(HttpClient);


    loadHeroItems(): Observable<HeroItemModel> {
        return this.http.get<HeroItemModel>(this.baseUrl + '/public/hero-items');
    }

    loadAnnouncements(): Observable<AnnouncementsModel[]> {
        return this.http.get<AnnouncementsModel[]>(this.baseUrl + '/public/announcements');
    }


    loadGigs(): Observable<ApiResponse<GigModel[]>> {
        return this.http.get<ApiResponse<GigModel[]>>(this.baseUrl + '/public/gigs');
    }

    loadMembers(): Observable<ApiResponse<MemberModel[]>> {
        return this.http.get<ApiResponse<MemberModel[]>>(this.baseUrl + '/public/members');
    }

    loadAbout(): Observable<ApiResponse<AboutModel>> {
        return this.http.get<ApiResponse<AboutModel>>(this.baseUrl + '/public/about');
    }
}
