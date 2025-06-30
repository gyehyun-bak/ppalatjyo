import type { QuizResponseDto } from '../types/api/quiz/QuizResponseDto';
import type { QuizzesResponseDto } from '../types/api/quiz/QuizzesResponseDto';
import type { ResponseDto } from '../types/api/ResponseDto';
import { api } from './axios';

export const getQuizzes = async (): Promise<
    ResponseDto<QuizzesResponseDto>
> => {
    return (await api.get<ResponseDto<QuizzesResponseDto>>('/quizzes')).data;
};

export const getQuiz = async (
    quizId: number
): Promise<ResponseDto<QuizResponseDto>> => {
    return (
        await api.get<ResponseDto<QuizResponseDto>>(`/quizzes/${quizId}`, {
            params: { includeQuestions: true },
        })
    ).data;
};
