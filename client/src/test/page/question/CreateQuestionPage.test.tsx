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

        const contentInput = screen.getByTestId('content-input');
        const answerInput = screen.getByTestId('answer-input');
        const addAnswerButton = screen.getByTestId('add-answer-button');
        const saveButton = screen.getByTestId('save-button');

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
    it('"답" 입력 칸에 추가할 답을 입력하고 "추가" 버튼을 클릭하면 생성할 답 목록에 추가된다', async () => {
        // given
        // when
        // then
    });
    it('추가된 답의 "X" 버튼을 누르면 해당 답을 삭제한다', async () => {
        // given
        // when
        // then
    });
    it('추가된 답 없이 "저장하기" 버튼을 누르면 "최소 1개의 답이 있어야 합니다" 경고 토스트를 띄운다', async () => {
        // given
        // when
        // then
    });
});
