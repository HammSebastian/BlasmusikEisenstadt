import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {catchError, retry, shareReplay} from 'rxjs/operators';
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
import {GalleryModel} from '../models/gallery.model';
import {ImageModel} from '../models/image.model';

@Injectable({
  providedIn: 'root'
})
export class DataService {

    private readonly environment = environment.apiUrl;
    private readonly http = inject(HttpClient);

    /**
     * Applies retry logic and caching to an observable
     * @param observable The observable to enhance
     * @param cacheTime The time in milliseconds to cache the response (default: 5 minutes)
     * @param retryCount The number of times to retry the request (default: 3)
     * @returns The enhanced observable
     */
    private enhanceRequest<T>(observable: Observable<T>, cacheTime = 300000, retryCount = 3): Observable<T> {
        return observable.pipe(
            retry(retryCount),
            shareReplay({ bufferSize: 1, refCount: true, windowTime: cacheTime })
        );
    }

    //-------------------------------------| Welcome home |-------------------------------------
    /**
     * Loads the welcome data for the home page
     * @returns An observable with the welcome data
     */
    public loadHomeWelcomeData(): Observable<ApiResponse<HomeWelcomeModel>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<HomeWelcomeModel>>(`${this.environment}/welcome`)
        );
    }

    public putHomeWelcomeData(): Observable<ApiResponse<HomeWelcomeModel>> {
        return this.http.put<ApiResponse<HomeWelcomeModel>>(`${this.environment}/welcome`, {});
    }

    public uploadHomeWelcomeFile(): Observable<ApiResponse<HomeWelcomeModel>> {
        return this.http.post<ApiResponse<HomeWelcomeModel>>(`${this.environment}/welcome/upload-image`, {});
    }
    //-------------------------------------| Welcome home |-------------------------------------

    //-------------------------------------| About us |-------------------------------------
    /**
     * Loads the about us data
     * @returns An observable with the about us data
     */
    public loadAboutData(): Observable<ApiResponse<AboutModel>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<AboutModel>>(`${this.environment}/about`)
        );
    }

    public putAboutData(): Observable<ApiResponse<AboutModel>> {
        return this.http.put<ApiResponse<AboutModel>>(`${this.environment}/about`, {});
    }

    public uploadAboutFile(): Observable<ApiResponse<AboutModel>> {
        return this.http.post<ApiResponse<AboutModel>>(`${this.environment}/about/upload-image`, {});
    }
    //-------------------------------------| About us |-------------------------------------

    //-------------------------------------| Events |-------------------------------------
    /**
     * Loads all events data
     * @returns An observable with the events data
     */
    public loadEventsData(): Observable<ApiResponse<EventModel[]>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<EventModel[]>>(`${this.environment}/events`)
        );
    }

    /**
     * Loads an event by its ID
     * @param id The event ID
     * @returns An observable with the event data
     */
    public loadEventById(id: number): Observable<ApiResponse<EventModel>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<EventModel>>(`${this.environment}/events/${id}`)
        );
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
    /**
     * Loads all locations data
     * @returns An observable with the locations data
     */
    public loadLocationsData(): Observable<ApiResponse<LocationModel[]>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<LocationModel[]>>(`${this.environment}/locations`)
        );
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

    /**
     * Loads a location by its ID
     * @param id The location ID
     * @returns An observable with the location data
     */
    public loadLocationById(id: number): Observable<ApiResponse<LocationModel>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<LocationModel>>(`${this.environment}/locations/${id}`)
        );
    }

    //-------------------------------------| Locations |-------------------------------------

    //-------------------------------------| News |-------------------------------------
    /**
     * Loads all news data
     * @returns An observable with the news data
     */
    public loadNewsData(): Observable<ApiResponse<NewsModel[]>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<NewsModel[]>>(`${this.environment}/news`)
        );
    }

    //-------------------------------------| News |-------------------------------------

    //-------------------------------------| History |-------------------------------------
    /**
     * Loads the history data
     * @returns An observable with the history data
     */
    public loadHistoryData(): Observable<ApiResponse<HistoryModel>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<HistoryModel>>(`${this.environment}/history`)
        );
    }

    //-------------------------------------| History |-------------------------------------

    //-------------------------------------| Members |-------------------------------------
    /**
     * Loads all members
     * @returns An observable with the members data
     */
    public loadMembers(): Observable<ApiResponse<MembersModel[]>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<MembersModel[]>>(`${this.environment}/members`)
        );
    }

    /**
     * Creates a new member
     * @param member The member data to create
     * @returns An observable with the created member
     */
    public createMember(member: MembersModel): Observable<ApiResponse<MembersModel>> {
        return this.http.post<ApiResponse<MembersModel>>(`${this.environment}/members`, member);
    }

    //-------------------------------------| Members |-------------------------------------

    //-------------------------------------| Gallery |-------------------------------------
    /**
     * Loads all galleries
     * @returns An observable with the galleries data
     */
    public loadGallery(): Observable<ApiResponse<GalleryModel[]>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<GalleryModel[]>>(`${this.environment}/gallery`)
        );
    }

    /**
     * Loads gallery images by slug
     * @param slug The gallery slug
     * @returns An observable with the gallery images
     */
    public loadGalleryImagesBySlug(slug: string): Observable<ApiResponse<ImageModel[]>> {
        return this.enhanceRequest(
            this.http.get<ApiResponse<ImageModel[]>>(`${this.environment}/images/slug/${slug}`)
        );
    }

    //-------------------------------------| Gallery |-------------------------------------
}
