import type { CreateQuizRequest } from './types/quiz/CreateQuizRequest';
import type { QuizResponse } from './types/quiz/QuizResponse';
import type { QuizzesResponse } from './types/quiz/QuizzesResponse';
import { api } from './axios';

export const getQuizzes = async (): Promise<QuizzesResponse> => {
    return (await api.get<QuizzesResponse>('/quizzes')).data;
};

export const getQuiz = async (
    quizId: number | string,
    includeQuestions: boolean = false
): Promise<QuizResponse> => {
    return (
        await api.get<QuizResponse>(`/quizzes/${quizId}`, {
            params: { includeQuestions: includeQuestions }, // 서버 측 구현 필요
        })
    ).data;
};

export const createQuiz = async (
    data: CreateQuizRequest
): Promise<QuizResponse> => {
    return (
        await api.post<QuizResponse>(`/quizzes`, {
            data,
        })
    ).data;
};
