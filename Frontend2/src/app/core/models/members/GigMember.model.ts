export interface GigMemberModel {
    id: string;
    title: string;
    date: string;
    time: string;
    venue: string;
    address: string;
    description: string;
    status: 'upcoming' | 'completed' | 'cancelled';
    imageUrl: string;
    attendees: number;
    maxAttendees: number;
    userAttendance?: 'confirmed' | 'declined' | 'pending';
    userAttendanceReason?: string;
}
