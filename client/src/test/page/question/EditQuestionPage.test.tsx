import { screen, waitFor } from '@testing-library/dom';
import EditQuestionPage from '../../../page/question/EditQuestionPage';
import { renderWithWrapper } from '../../utils/renderWithWrapper';
import { baseUrl, server } from '../../../mocks/server';
import { http, HttpResponse } from 'msw';
import {
    mockNavigate,
    mockUseParams,
} from '../../../../__mocks__/react-router';
import type { QuestionResponse } from '../../../api/types/quiz/QuestionResponse';
import type { EditQuestionRequest } from '../../../api/types/question/EditQuestionRequest';
import userEvent, { type UserEvent } from '@testing-library/user-event';

const { mockAddToast } = vi.hoisted(() => ({
    mockAddToast: vi.fn(),
}));

vi.mock('@heroui/react', async () => {
    const actual = await vi.importActual<typeof import('@heroui/react')>(
        '@heroui/react'
    );
    return {
        ...actual,
        addToast: mockAddToast,
    };
});

vi.mock('react-router');

describe('EditQuestionPage', () => {
    const quizId = 123;
    const questionId = 456;

    let user: UserEvent;

    beforeEach(() => {
        user = userEvent.setup();
        mockUseParams.mockReturnValue({ quizId, questionId });
    });

    it('마운트 시 쿼리 파라미터의 questionId로 퀴즈 정보 불러와 입력 및 답 목록에 표시한다', async () => {
        // given
        const content = 'content1234';
        const answer1 = 'answer1';
        const answer2 = 'answer2';

        server.use(
            http.get<never, never, QuestionResponse>(
                `${baseUrl}/quizzes/${quizId}/questions/${questionId}?includeAnswers=true`,
                () =>
                    HttpResponse.json({
                        id: questionId,
                        content: content,
                        answers: [
                            {
                                id: 0,
                                content: answer1,
                            },
                            {
                                id: 1,
                                content: answer2,
                            },
                        ],
                    })
            )
        );

        // when
        renderWithWrapper(<EditQuestionPage />);

        // then
        const contentInput = await screen.findByTestId('content-input');

        await waitFor(() => {
            expect(contentInput).toHaveValue(content);
        });

        expect(await screen.findByText(answer1)).toBeVisible();
        expect(await screen.findByText(answer2)).toBeVisible();
    });

    it('"저장하기" 버튼을 누르면 업데이트를 요청하고 성공 시 "문제 상세 보기"로 이동한다', async () => {
        // given
        server.use(
            http.get<never, never, QuestionResponse>(
                `${baseUrl}/quizzes/${quizId}/questions/${questionId}?includeAnswers=true`,
                () =>
                    HttpResponse.json({
                        id: questionId,
                        content: 'question',
                        answers: [
                            {
                                id: 0,
                                content: 'answer1',
                            },
                        ],
                    })
            ),
            http.put<never, EditQuestionRequest, QuestionResponse>(
                `${baseUrl}/quizzes/${quizId}/questions/${questionId}`,
                () =>
                    HttpResponse.json({
                        id: questionId,
                        content: '',
                        answers: [],
                    })
            )
        );

        renderWithWrapper(<EditQuestionPage />);

        const saveButton = await screen.findByTestId('save-button');

        // when
        await user.click(saveButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith(
                `/quizzes/${quizId}/questions/${questionId}`
            );
        });
    });

    it('답이 하나 이상 있지 않으면 경고 토스트를 표시한다.', async () => {
        // given
        const answerId = 0;

        server.use(
            http.get<never, never, QuestionResponse>(
                `${baseUrl}/quizzes/${quizId}/questions/${questionId}?includeAnswers=true`,
                () =>
                    HttpResponse.json({
                        id: questionId,
                        content: 'question123',
                        answers: [
                            {
                                id: answerId,
                                content: 'answer123',
                            },
                        ],
                    })
            )
        );

        renderWithWrapper(<EditQuestionPage />);

        const deleteAnswerButtons = await screen.findAllByTestId(
            'delete-answer-button'
        );
        const saveButton = await screen.findByTestId('save-button');

        // when
        await user.click(deleteAnswerButtons[0]);
        await user.click(saveButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).not.toHaveBeenCalled();
            expect(mockAddToast).toHaveBeenCalled();
        });
    });

    it('"삭제하기" 버튼을 클릭하면 모달창 표시, 모달창의 "삭제하기" 버튼을 클릭하면 삭제를 요청하고 "퀴즈 상세 보기"로 이동한다', async () => {
        // given
        server.use(
            http.delete(
                `${baseUrl}/quizzes/${quizId}/questions/${questionId}`,
                () => HttpResponse.text('삭제 완료')
            )
        );

        renderWithWrapper(<EditQuestionPage />);

        const deleteButton = await screen.findByTestId('delete-button');
        await user.click(deleteButton);
        const confirmDeleteButton = await screen.findByTestId(
            'confirm-delete-button'
        );

        // when
        await user.click(confirmDeleteButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toBeCalledWith(`/quizzes/${quizId}`);
        });
    });
});
