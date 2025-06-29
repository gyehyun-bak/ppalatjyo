import type { LobbiesResponseDto } from '../types/api/lobby/LobbiesResponseDto';
import type { ResponseDto } from '../types/api/ResponseDto';
import { api } from './axios';

export const getLobbies = async (): Promise<
    ResponseDto<LobbiesResponseDto>
> => {
    return (await api.get<ResponseDto<LobbiesResponseDto>>('/lobbies')).data;
};
