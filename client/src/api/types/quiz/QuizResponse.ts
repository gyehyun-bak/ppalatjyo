import type { QuestionResponse } from "./QuestionResponse";
import type { QuizVisibility } from "./QuizVisibility";

export interface QuizResponse {
    id: number;
    title: string;
    authorNickname: string;
    description: string;
    totalQuestions: number;
    visibility: QuizVisibility;
    questions: QuestionResponse[];
}
