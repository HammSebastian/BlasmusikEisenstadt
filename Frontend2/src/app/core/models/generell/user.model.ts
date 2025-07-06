export interface User {
    id: string;
    name: string;
    email: string;
    role: 'admin' | 'reporter' | 'conductor' | 'section-leader' | 'musician';
    status: 'active' | 'inactive';
    lastLogin: string;
    joinDate: string;
}
