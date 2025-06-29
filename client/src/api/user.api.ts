import type { ResponseDto } from "../types/api/ResponseDto";
import type { UserResponseDto } from "../types/api/user/UserResponseDto";
import { api } from "./axios";

export const getMe = async (): Promise<ResponseDto<UserResponseDto>> => {
    return (await api.get<ResponseDto<UserResponseDto>>("/users/me")).data;
};
