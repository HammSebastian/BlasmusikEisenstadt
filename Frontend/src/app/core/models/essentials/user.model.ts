import {RoleEnum} from './role.enum';

export interface UserModel {
    id: string;
    name: string;
    role: RoleEnum;
    email: string;
    status: 'active' | 'inactive';
    lastLogin: string;
    joinDate: string;
}
