export interface Section {
    year: number;
    description: string;
}

export interface HistoryModel {
    name: string;
    sections: Section[];
}
