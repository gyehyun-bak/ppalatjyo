import { userEvent, type UserEvent } from '@testing-library/user-event';
import { mockUseParams } from '../../../../__mocks__/react-router';
import { baseUrl, server } from '../../../mocks/server';
import { http, HttpResponse } from 'msw';
import type { QuestionResponse } from '../../../api/types/quiz/QuestionResponse';
import QuestionDetailsPage from '../../../page/question/QuestionDetailsPage';
import { renderWithWrapper } from '../../utils/renderWithWrapper';
import { screen, waitFor } from '@testing-library/dom';

vi.mock('react-router');

describe('QuestionDetailsPage', () => {
    const quizId = 123;
    const questionId = 456;

    let user: UserEvent;

    beforeEach(() => {
        user = userEvent.setup();
        mockUseParams.mockReturnValue({ quizId, questionId });
    });

    it('마운트 시 쿼리 파라미터의 questionId로 퀴즈 정보 불러와 내용 및 답 목록에 표시한다', async () => {
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
        renderWithWrapper(<QuestionDetailsPage />);

        // then
        const contentInput = await screen.findByTestId('content-input');

        await waitFor(() => {
            expect(contentInput).toHaveValue(content);
        });

        expect(await screen.findByText(answer1)).toBeVisible();
        expect(await screen.findByText(answer2)).toBeVisible();
    });
    it('"수정하기" 버튼을 누르면 "문제 수정하기" 페이지로 이동한다', async () => {});
});
