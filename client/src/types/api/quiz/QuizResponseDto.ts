import type { QuestionResponseDto } from "./QuestionResponseDto";

export interface QuizResponseDto {
    id: number;
    title: string;
    authorNickname: string;
    totalQuestions: number;
    questions: QuestionResponseDto[];
}
