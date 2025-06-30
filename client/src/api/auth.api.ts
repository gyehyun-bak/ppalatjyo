import type { JoinAsGuestRequestDto } from '../types/api/auth/JoinAsGuestRequestDto';
import type { JoinAsGuestResponseDto } from '../types/api/auth/JoinAsGuestResponseDto';
import type { TokenReissueResponseDto } from '../types/api/auth/TokenReissueResponseDto';
import type { ResponseDto } from '../types/api/ResponseDto';
import { api } from './axios';

export const postSignUpGuest = async (
    requestDto: JoinAsGuestRequestDto
): Promise<ResponseDto<JoinAsGuestResponseDto>> => {
    return (
        await api.post<ResponseDto<JoinAsGuestResponseDto>>(
            '/auth/sign-up/guest',
            requestDto
        )
    ).data;
};

export const getNewTokens = async (): Promise<
    ResponseDto<TokenReissueResponseDto>
> => {
    return (await api.get<ResponseDto<JoinAsGuestResponseDto>>('/auth/tokens'))
        .data;
};
