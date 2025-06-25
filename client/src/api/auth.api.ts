import type { JoinAsGuestRequestDto } from '../dto/auth/JoinAsGuestRequestDto';
import type { JoinAsGuestResponseDto } from '../dto/auth/JoinAsGuestResponseDto';
import type { ResponseDto } from '../dto/ResponseDto';
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
