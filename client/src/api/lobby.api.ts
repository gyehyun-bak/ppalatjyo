import type { CreateLobbyRequest } from './types/lobby/CreateLobbyRequest';
import type { LobbiesResponse } from './types/lobby/LobbiesResponse';
import type { LobbyResponse } from './types/lobby/LobbyResponse';
import { api } from './axios';

export const getLobbies = async (): Promise<LobbiesResponse> => {
    return (await api.get<LobbiesResponse>('/lobbies')).data;
};

export const createLobby = async ({
    name,
    maxUsers,
    minPerGame,
    secPerQuestion,
    quizId,
}: CreateLobbyRequest): Promise<LobbyResponse> => {
    return (
        await api.post<LobbyResponse>('/lobbies', {
            name,
            maxUsers,
            minPerGame,
            secPerQuestion,
            quizId,
        })
    ).data;
};
