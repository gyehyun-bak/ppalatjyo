import type { Quiz } from '../quiz/Quiz';
import type { User } from '../user/User';
import type { LobbyOptions } from './LobbyOptions';

export interface Lobby {
    id: number;
    name: string;
    currentUserCount: number;
    options: LobbyOptions;
    host: User;
    quiz: Quiz;
}
