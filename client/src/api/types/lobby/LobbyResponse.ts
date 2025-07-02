import type { QuizResponse } from '../quiz/QuizResponse';
import type { UserResponse } from '../user/UserResponse';
import type { LobbyOptions } from './LobbyOptions';

export interface LobbyResponse {
    id: number;
    name: string;
    currentUserCount: number;
    options: LobbyOptions;
    host: UserResponse;
    quiz: QuizResponse;
}
