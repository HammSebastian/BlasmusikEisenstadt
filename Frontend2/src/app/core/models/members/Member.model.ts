export interface Member {
    id: string;
    name: string;
    email: string;
    phone: string;
    instrument: string;
    section: string;
    joinDate: string;
    status: 'active' | 'inactive' | 'on-leave';
    avatarUrl: string;
    address: string;
    emergencyContact: string;
    emergencyPhone: string;
}
