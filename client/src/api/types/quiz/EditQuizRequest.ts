import type { QuizVisibility } from "./QuizVisibility";

export interface EditQuizRequest {
    title: string;
    description: string;
    visibility: QuizVisibility;
}
