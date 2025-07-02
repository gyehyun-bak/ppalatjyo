import type { JoinAsGuestRequest } from './types/auth/JoinAsGuestRequest';
import type { JoinAsGuestResponse } from './types/auth/JoinAsGuestResponse';
import type { TokenReissueResponse } from './types/auth/TokenReissueResponse';
import { api } from './axios';

export const postSignUpGuest = async (
    requestDto: JoinAsGuestRequest
): Promise<JoinAsGuestResponse> => {
    return (
        await api.post<JoinAsGuestResponse>('/auth/sign-up/guest', requestDto)
    ).data;
};

export const getNewTokens = async (): Promise<TokenReissueResponse> => {
    return (await api.get<JoinAsGuestResponse>('/auth/tokens')).data;
};
