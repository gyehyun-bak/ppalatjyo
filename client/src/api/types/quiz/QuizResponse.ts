import type { QuestionResponse } from './QuestionResponse';

export interface QuizResponse {
    id: number;
    title: string;
    authorNickname: string;
    description: string;
    totalQuestions: number;
    questions: QuestionResponse[];
}
