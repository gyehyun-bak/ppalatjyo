import { http, HttpResponse } from 'msw';
import { baseUrl, server } from '../../../mocks/server';
import type { QuizResponse } from '../../../api/types/quiz/QuizResponse';
import type { QuestionResponse } from '../../../api/types/quiz/QuestionResponse';
import { renderWithWrapper } from '../../utils/renderWithWrapper';
import QuizDetailsPage from '../../../page/quiz/QuizDetailsPage';
import { screen, waitFor } from '@testing-library/dom';
import {
    mockNavigate,
    mockUseParams,
} from '../../../../__mocks__/react-router';
import userEvent from '@testing-library/user-event';

vi.mock('react-router');

describe('QuizDetailsPage', () => {
    it('쿼리 파라미터의 quizId로 퀴즈 이름, 작성자 이름, 퀴즈 설명, 문제를 불러와 표시합니다', async () => {
        //given
        const quizId = 123;
        mockUseParams.mockReturnValue({ quizId });

        const title = 'quiz123';
        const authorNickname = 'hello';
        const description = 'description123';
        const totalQuestions = 2;
        const questions: QuestionResponse[] = [
            {
                id: 0,
                content: 'question1',
                answers: [],
            },
            {
                id: 1,
                content: 'question2',
                answers: [],
            },
        ];

        server.use(
            http.get<never, never, QuizResponse>(
                `${baseUrl}/quizzes/${quizId}?includeQuestions=true`,
                () =>
                    HttpResponse.json({
                        id: quizId,
                        title: title,
                        authorNickname: authorNickname,
                        description: description,
                        totalQuestions: totalQuestions,
                        questions: questions,
                    })
            )
        );

        // when
        renderWithWrapper(<QuizDetailsPage />);

        // then
        await waitFor(() => {
            expect(screen.getByText(title)).toBeInTheDocument();
            expect(screen.getByText(authorNickname)).toBeInTheDocument();
            expect(screen.getByText(totalQuestions)).toBeInTheDocument();
            expect(screen.getByText(description)).toBeInTheDocument();
            expect(screen.getByText(questions[0].content)).toBeInTheDocument();
            expect(screen.getByText(questions[1].content)).toBeInTheDocument();
        });
    });

    it('각 문제를 클릭하면 해당 문제 상세보기 페이지로 이동합니다', async () => {
        //given
        const quizId = 123;
        const questionId = 456;
        mockUseParams.mockReturnValue({ quizId });

        const questions: QuestionResponse[] = [
            { id: questionId, content: 'question1', answers: [] },
        ];

        const response = {
            id: quizId,
            title: 'quiz123',
            authorNickname: 'hello',
            description: 'description123',
            totalQuestions: 1,
            questions,
        };

        server.use(
            http.get<never, never, QuizResponse>(
                `${baseUrl}/quizzes/${quizId}?includeQuestions=true`,
                () => HttpResponse.json(response)
            )
        );

        renderWithWrapper(<QuizDetailsPage />);

        const question1Button = await screen.findByTestId(
            `question-${questionId}`
        );
        const user = userEvent.setup();

        // when
        await user.click(question1Button);

        // then
        await waitFor(() => {
            expect(mockNavigate).toBeCalledWith(
                `/quizzes/${quizId}/questions/${questionId}`
            );
        });
    });
    it('<문제 추가> 버튼을 클릭하면 문제 생성 페이지로 이동합니다', async () => {
        //given
        const quizId = 123;
        mockUseParams.mockReturnValue({ quizId });

        const questions: QuestionResponse[] = [
            { id: 0, content: 'question1', answers: [] },
        ];

        const response = {
            id: quizId,
            title: 'quiz123',
            authorNickname: 'hello',
            description: 'description123',
            totalQuestions: 1,
            questions,
        };

        server.use(
            http.get<never, never, QuizResponse>(
                `${baseUrl}/quizzes/${quizId}?includeQuestions=true`,
                () => HttpResponse.json(response)
            )
        );

        renderWithWrapper(<QuizDetailsPage />);

        const createQuestionButton = await screen.findByTestId(
            'create-question'
        );
        const user = userEvent.setup();

        // when
        await user.click(createQuestionButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toBeCalledWith(
                `/quizzes/${quizId}/questions/create`
            );
        });
    });
    it('<수정하기> 버튼을 클릭하면 해당 퀴즈 수정 페이지로 이동합니다', () => {});
    it('<로비 만들기> 버튼을 클릭하면, 해당 퀴즈 아이디를 쿼리 파라미터로 갖는 로비 만들기 페이지로 이동합니다', () => {});
});
