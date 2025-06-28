import type { JoinAsGuestRequestDto } from "../types/api/auth/JoinAsGuestRequestDto";
import type { JoinAsGuestResponseDto } from "../types/api/auth/JoinAsGuestResponseDto";
import type { ResponseDto } from "../types/api/ResponseDto";
import { api } from "./axios";

export const postSignUpGuest = async (
    requestDto: JoinAsGuestRequestDto
): Promise<ResponseDto<JoinAsGuestResponseDto>> => {
    return (
        await api.post<ResponseDto<JoinAsGuestResponseDto>>(
            "/auth/sign-up/guest",
            requestDto
        )
    ).data;
};
