export interface AboutModel {
    id: number;
    imageUrl: string;
    story: string;
    mission: MissionItem[];
}

export interface MissionItem {
    title: string;
    description: string;
}
