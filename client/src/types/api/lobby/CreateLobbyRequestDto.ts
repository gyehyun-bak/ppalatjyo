export interface CreateLobbyRequestDto {
    name: string;
    password?: string;
    maxUsers: number;
    minPerGame: number;
    secPerQuestion: number;
    quizId: number | string;
}
