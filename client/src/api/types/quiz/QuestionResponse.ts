import type { AnswerResponse } from './AnswerResponse';

export interface QuestionResponse {
    id: number;
    content: string;
    answers: AnswerResponse[];
}
