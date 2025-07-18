import userEvent from '@testing-library/user-event';
import CreateQuestionPage from '../../../page/question/CreateQuestionPage';
import { renderWithWrapper } from '../../utils/renderWithWrapper';
import { screen, waitFor } from '@testing-library/dom';
import {
    mockNavigate,
    mockUseParams,
} from '../../../../__mocks__/react-router';
import { baseUrl, server } from '../../../mocks/server';
import { http, HttpResponse } from 'msw';
import type { QuestionResponse } from '../../../api/types/quiz/QuestionResponse';
import type { CreateQuestionRequest } from '../../../api/types/question/CreateQuestionRequest';

const { mockAddToast } = vi.hoisted(() => ({
    mockAddToast: vi.fn(),
}));

vi.mock('react-router');

vi.mock('@heroui/react', async () => {
    const actual = await vi.importActual<typeof import('@heroui/react')>(
        '@heroui/react'
    );
    return {
        ...actual,
        addToast: mockAddToast,
    };
});

describe('CreateQuestionPage', () => {
    const user = userEvent.setup();
    const quizId = 123;

    beforeEach(() => {
        mockUseParams.mockReturnValue({ quizId });
    });

    it('문제와 답을 입력하고 "저장하기" 버튼을 클릭하면 문제 생성 요청 후 성공 시 "문제 상세 보기" 페이지로 이동한다', async () => {
        // given
        server.use(
            http.post<never, CreateQuestionRequest, QuestionResponse>(
                `${baseUrl}/quizzes/${quizId}/questions`,
                () =>
                    HttpResponse.json({
                        id: questionId,
                        content: '',
                        answers: [],
                    })
            )
        );
        const questionId = 123;
        renderWithWrapper(<CreateQuestionPage />);

        const contentInput = await screen.findByTestId('content-input');
        const answerInput = await screen.findByTestId('answer-input');
        const addAnswerButton = await screen.findByTestId('add-answer-button');
        const saveButton = await screen.findByTestId('save-button');

        // when
        await user.type(contentInput, 'content');
        await user.type(answerInput, 'answer');
        await user.click(addAnswerButton);
        await user.click(saveButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith(
                `/quizzes/${quizId}/questions/${questionId}`
            );
        });
    });
    it('추가된 답 없이 "저장하기" 버튼을 누르면 "최소 1개의 답이 있어야 합니다" 경고 토스트를 띄운다', async () => {
        server.use(
            http.post<never, CreateQuestionRequest, QuestionResponse>(
                `${baseUrl}/quizzes/${quizId}/questions`,
                () =>
                    HttpResponse.json({
                        id: questionId,
                        content: '',
                        answers: [],
                    })
            )
        );
        const questionId = 123;
        renderWithWrapper(<CreateQuestionPage />);

        const contentInput = await screen.findByTestId('content-input');
        const saveButton = await screen.findByTestId('save-button');

        // when
        await user.type(contentInput, 'content');
        await user.click(saveButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).not.toHaveBeenCalled();
            expect(mockAddToast).toHaveBeenCalled();
        });
    });
});
