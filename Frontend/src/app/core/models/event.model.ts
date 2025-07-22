import { EventType } from "./enums/eventType.enum";
import {LocationModel} from './location.model';

export interface EventModel {
    id: number;
    eventImage: string;
    title: string;
    description: string;
    date: string;
    type: EventType;
    location: LocationModel;
}
