import { UserModel } from './user.model';

export interface AuthResponse {
    user: UserModel;
    token: string;
    refreshToken: string;
}
