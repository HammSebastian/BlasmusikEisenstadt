import {MissionItem} from './mission.model';

export interface AboutModel {
    id: number;
    imageUrl: string;
    story: string;
    missions: MissionItem[];  // Changed from 'mission' to 'missions' to match API
}
