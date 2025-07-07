import { api } from './axios';
import type { CreateQuestionRequest } from './types/question/CreateQuestionRequest';
import type { QuestionResponse } from './types/quiz/QuestionResponse';

export const createQuestion = async (
    quizId: string | number,
    data: CreateQuestionRequest
): Promise<QuestionResponse> => {
    return (
        await api.post<QuestionResponse>(`/quizzes/${quizId}/questions`, {
            data,
        })
    ).data;
};
