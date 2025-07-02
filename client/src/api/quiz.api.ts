import type { CreateQuizRequestDto } from '../types/api/quiz/CreateQuizRequestDto';
import type { QuizResponseDto } from '../types/api/quiz/QuizResponseDto';
import type { QuizzesResponseDto } from '../types/api/quiz/QuizzesResponseDto';
import { api } from './axios';

export const getQuizzes = async (): Promise<QuizzesResponseDto> => {
    return (await api.get<QuizzesResponseDto>('/quizzes')).data;
};

export const getQuiz = async (quizId: number): Promise<QuizResponseDto> => {
    return (
        await api.get<QuizResponseDto>(`/quizzes/${quizId}`, {
            params: { includeQuestions: true }, // 서버 측 구현 필요
        })
    ).data;
};

export const createQuiz = async (
    data: CreateQuizRequestDto
): Promise<QuizResponseDto> => {
    return (
        await api.post<QuizResponseDto>(`/quizzes`, {
            data,
        })
    ).data;
};
