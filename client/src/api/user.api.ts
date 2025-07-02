import type { UserResponse } from './types/user/UserResponse';
import { api } from './axios';

export const getMe = async (): Promise<UserResponse> => {
    return (await api.get<UserResponse>('/users/me')).data;
};
