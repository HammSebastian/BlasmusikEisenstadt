export interface Message {
    id: string;
    title: string;
    preview: string;
    time: Date;
    read: boolean;
    sender: {
        id: string;
        name: string;
        avatar?: string;
    };
}
