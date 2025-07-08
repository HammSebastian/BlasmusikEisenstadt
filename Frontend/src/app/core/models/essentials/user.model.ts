import { RoleEnum } from './role.enum';

export interface UserModel {
    id: string;
    name: string;
    roles: RoleEnum[];  // Ändere role zu roles als Array
    email: string;
}
