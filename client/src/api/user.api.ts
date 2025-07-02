import type { UserResponseDto } from '../types/api/user/UserResponseDto';
import { api } from './axios';

export const getMe = async (): Promise<UserResponseDto> => {
    return (await api.get<UserResponseDto>('/users/me')).data;
};
