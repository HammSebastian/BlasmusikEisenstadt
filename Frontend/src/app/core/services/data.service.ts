import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ApiResponse} from '../models/apiResponse.model';
import {HomeWelcomeModel} from '../models/homeWelcome.model';
import {HttpClient} from '@angular/common/http';
import {AboutModel} from '../models/about.model';
import {EventModel} from '../models/event.model';
import {LocationModel} from '../models/location.model';
import {environment} from '../../../environments/environment';
import {NewsModel} from '../models/news.model';
import {HistoryModel} from '../models/history.model';
import {MembersModel} from '../models/members.model';

@Injectable({
  providedIn: 'root'
})
export class DataService {

    private readonly environment = environment.apiUrl;
    private readonly http = inject(HttpClient);

    //-------------------------------------| Welcome home |-------------------------------------
    public loadHomeWelcomeData(): Observable<ApiResponse<HomeWelcomeModel>> {
        return this.http.get<ApiResponse<HomeWelcomeModel>>(`${this.environment}/welcomecontent`);
    }

    public putHomeWelcomeData(): Observable<ApiResponse<HomeWelcomeModel>> {
        return this.http.put<ApiResponse<HomeWelcomeModel>>(`${this.environment}/welcomecontent`, {});
    }

    public uploadHomeWelcomeFile(): Observable<ApiResponse<HomeWelcomeModel>> {
        return this.http.post<ApiResponse<HomeWelcomeModel>>(`${this.environment}/welcomecontent/upload-image`, {});
    }
    //-------------------------------------| Welcome home |-------------------------------------

    //-------------------------------------| About us |-------------------------------------
    public loadAboutData(): Observable<ApiResponse<AboutModel>> {
        return this.http.get<ApiResponse<AboutModel>>(`${this.environment}/about`);
    }

    public putAboutData(): Observable<ApiResponse<AboutModel>> {
        return this.http.put<ApiResponse<AboutModel>>(`${this.environment}/about`, {});
    }

    public uploadAboutFile(): Observable<ApiResponse<AboutModel>> {
        return this.http.post<ApiResponse<AboutModel>>(`${this.environment}/about/upload-image`, {});
    }
    //-------------------------------------| About us |-------------------------------------

    //-------------------------------------| Events |-------------------------------------
    public loadEventsData(): Observable<ApiResponse<EventModel[]>> {
        return this.http.get<ApiResponse<EventModel[]>>(`${this.environment}/events`);
    }

    public loadEventById(id: number): Observable<ApiResponse<EventModel>> {
        return this.http.get<ApiResponse<EventModel>>(`${this.environment}/events/${id}`);
    }

    public createEvent(event: EventModel): Observable<ApiResponse<EventModel>> {
        return this.http.post<ApiResponse<EventModel>>(`${this.environment}/events`, event);
    }

    public putEventsData(id: number): Observable<ApiResponse<EventModel>> {
        return this.http.put<ApiResponse<EventModel>>(`${this.environment}/events/${id}`, {});
    }

    public getUploadEventsFile(): Observable<ApiResponse<EventModel>> {
        return this.http.get<ApiResponse<EventModel>>(`${this.environment}/events/upload-image`);
    }

    public uploadEventsFile(): Observable<ApiResponse<EventModel>> {
        return this.http.post<ApiResponse<EventModel>>(`${this.environment}/events/upload-image`, {});
    }

    public deleteEventsData(): Observable<ApiResponse<EventModel>> {
        return this.http.delete<ApiResponse<EventModel>>(`${this.environment}/events`);
    }
    //-------------------------------------| Events |-------------------------------------

    //-------------------------------------| Locations |-------------------------------------
    public loadLocationsData(): Observable<ApiResponse<LocationModel[]>> {
        return this.http.get<ApiResponse<LocationModel[]>>(`${this.environment}/locations`);
    }

    public createLocation(location: LocationModel): Observable<ApiResponse<LocationModel>> {
        return this.http.post<ApiResponse<LocationModel>>(`${this.environment}/locations`, location);
    }

    public putLocationsData(id: number): Observable<ApiResponse<LocationModel>> {
        return this.http.put<ApiResponse<LocationModel>>(`${this.environment}/locations/${id}`, {});
    }

    public getUploadLocationsFile(): Observable<ApiResponse<LocationModel>> {
        return this.http.get<ApiResponse<LocationModel>>(`${this.environment}/locations/upload-image`);
    }

    public uploadLocationsFile(): Observable<ApiResponse<LocationModel>> {
        return this.http.post<ApiResponse<LocationModel>>(`${this.environment}/locations/upload-image`, {});
    }

    public deleteLocationsData(): Observable<ApiResponse<LocationModel>> {
        return this.http.delete<ApiResponse<LocationModel>>(`${this.environment}/locations`);
    }

    public getLocationById(id: number): Observable<ApiResponse<LocationModel>> {
        return this.http.get<ApiResponse<LocationModel>>(`${this.environment}/locations/${id}`);
    }

    //-------------------------------------| Locations |-------------------------------------

    //-------------------------------------| News |-------------------------------------

    public loadNewsData(): Observable<ApiResponse<NewsModel[]>> {
        return this.http.get<ApiResponse<NewsModel[]>>(`${this.environment}/news`);
    }

    //-------------------------------------| News |-------------------------------------

    //-------------------------------------| History |-------------------------------------
    public loadHistoryData(): Observable<ApiResponse<HistoryModel>> {
        return this.http.get<ApiResponse<HistoryModel>>(`${this.environment}/history`);
    }

    //-------------------------------------| History |-------------------------------------

    //-------------------------------------| Members |-------------------------------------
    public loadMembers(): Observable<ApiResponse<MembersModel[]>> {
        return this.http.get<ApiResponse<MembersModel[]>>(`${this.environment}/members`);
    }

    //-------------------------------------| Members |-------------------------------------



}
