import type { JoinAsGuestRequestDto } from '../types/api/auth/JoinAsGuestRequestDto';
import type { JoinAsGuestResponseDto } from '../types/api/auth/JoinAsGuestResponseDto';
import type { TokenReissueResponseDto } from '../types/api/auth/TokenReissueResponseDto';
import { api } from './axios';

export const postSignUpGuest = async (
    requestDto: JoinAsGuestRequestDto
): Promise<JoinAsGuestResponseDto> => {
    return (
        await api.post<JoinAsGuestResponseDto>(
            '/auth/sign-up/guest',
            requestDto
        )
    ).data;
};

export const getNewTokens = async (): Promise<TokenReissueResponseDto> => {
    return (await api.get<JoinAsGuestResponseDto>('/auth/tokens')).data;
};
