import { api } from './axios';
import type { CreateQuestionRequest } from './types/question/CreateQuestionRequest';
import type { EditQuestionRequest } from './types/question/EditQuestionRequest';
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

export const getQuestion = async (
    quizId: string | number,
    questionId: string | number,
    includeAnswers: boolean = false
): Promise<QuestionResponse> => {
    return (
        await api.get<QuestionResponse>(
            `/quizzes/${quizId}/questions/${questionId}`,
            {
                params: { includeAnswers },
            }
        )
    ).data;
};

export const editQuestion = async (
    quizId: string | number,
    questionId: string | number,
    data: EditQuestionRequest
): Promise<QuestionResponse> => {
    return (
        await api.put<QuestionResponse>(
            `/quizzes/${quizId}/questions/${questionId}`,
            {
                data,
            }
        )
    ).data;
};
