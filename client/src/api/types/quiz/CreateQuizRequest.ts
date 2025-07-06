import type { QuizVisibility } from "./QuizVisibility";

export interface CreateQuizRequest {
    title: string;
    description: string;
    visibility: QuizVisibility;
}
