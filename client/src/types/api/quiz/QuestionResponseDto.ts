import type { AnswerResponseDto } from "./AnswerResponseDto";

export interface QuestionResponseDto {
    id: number;
    content: string;
    answers: AnswerResponseDto[];
}
