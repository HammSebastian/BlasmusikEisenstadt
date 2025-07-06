import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environment/environment';
import {Observable} from 'rxjs';
import {ApiResponse} from '../models/ApiResponse.model';
import {HeroItemModelModel} from '../models/HeroItemModel.model';
import {AnnouncementsModel} from '../models/Announcements.model';

@Injectable({
    providedIn: 'root'
})
export class DataService {

    baseUrl = environment.apiUrl;

    constructor(private readonly http: HttpClient) {
    }


    loadHeroItems(): Observable<ApiResponse<HeroItemModelModel[]>> {
        return this.http.get<ApiResponse<HeroItemModelModel[]>>(this.baseUrl + '/public-dashboard/hero-items');
    }

    loadAnnouncements(): Observable<ApiResponse<AnnouncementsModel[]>> {
        return this.http.get<ApiResponse<AnnouncementsModel[]>>(this.baseUrl + '/announcements');
    }
}
