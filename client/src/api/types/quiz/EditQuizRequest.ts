import type { QuizVisibility } from "./QuizVisibility";

export interface EditQuizRequest {
    id: number | string;
    title: string;
    description: string;
    visibility: QuizVisibility;
}
