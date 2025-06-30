import type { CreateLobbyRequestDto } from '../types/api/lobby/CreateLobbyRequestDto';
import type { LobbiesResponseDto } from '../types/api/lobby/LobbiesResponseDto';
import type { ResponseDto } from '../types/api/ResponseDto';
import type { Lobby } from '../types/lobby/Lobby';
import { api } from './axios';

export const getLobbies = async (): Promise<
    ResponseDto<LobbiesResponseDto>
> => {
    return (await api.get<ResponseDto<LobbiesResponseDto>>('/lobbies')).data;
};

export const createLobby = async ({
    name,
    maxUsers,
    minPerGame,
    secPerQuestion,
    quizId,
}: CreateLobbyRequestDto): Promise<ResponseDto<Lobby>> => {
    return (
        await api.post<ResponseDto<Lobby>>('/lobbies', {
            name,
            maxUsers,
            minPerGame,
            secPerQuestion,
            quizId,
        })
    ).data;
};
