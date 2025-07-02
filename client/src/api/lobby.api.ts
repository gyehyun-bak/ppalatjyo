import type { CreateLobbyRequestDto } from '../types/api/lobby/CreateLobbyRequestDto';
import type { LobbiesResponseDto } from '../types/api/lobby/LobbiesResponseDto';
import type { Lobby } from '../types/lobby/Lobby';
import { api } from './axios';

export const getLobbies = async (): Promise<LobbiesResponseDto> => {
    return (await api.get<LobbiesResponseDto>('/lobbies')).data;
};

export const createLobby = async ({
    name,
    maxUsers,
    minPerGame,
    secPerQuestion,
    quizId,
}: CreateLobbyRequestDto): Promise<Lobby> => {
    return (
        await api.post<Lobby>('/lobbies', {
            name,
            maxUsers,
            minPerGame,
            secPerQuestion,
            quizId,
        })
    ).data;
};
