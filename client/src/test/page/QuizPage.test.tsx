import { http, HttpResponse } from 'msw';
import { baseUrl, server } from '../../mocks/server';
import QuizPage from '../../page/QuizPage';
import { renderWithWrapper } from '../utils/renderWithWrapper';
import type { QuizzesResponseDto } from '../../types/api/quiz/QuizzesResponseDto';
import { screen, waitFor } from '@testing-library/dom';
import '@testing-library/jest-dom';
import userEvent from '@testing-library/user-event';
import { mockNavigate } from '../../../__mocks__/react-router';

vi.mock('react-router');

describe('QuizPage', () => {
    it('퀴즈 목록을 불러와 화면에 표시한다', async () => {
        // given
        server.use(
            http.get<never, never, QuizzesResponseDto>(
                baseUrl + '/quizzes',
                async () => {
                    return HttpResponse.json({
                        quizzes: [
                            {
                                id: 1,
                                title: 'quiz1',
                                authorNickname: 'user1',
                                totalQuestions: 10,
                            },
                            {
                                id: 2,
                                title: 'quiz2',
                                authorNickname: 'user2',
                                totalQuestions: 20,
                            },
                        ],
                    });
                }
            )
        );

        // when
        renderWithWrapper(<QuizPage />);

        // then
        await waitFor(() => {
            expect(screen.getByText(/quiz1/)).toBeInTheDocument();
            expect(screen.getByText(/quiz2/)).toBeInTheDocument();
            expect(screen.getByText(/user1/)).toBeInTheDocument();
            expect(screen.getByText(/user2/)).toBeInTheDocument();
            expect(screen.getByText(/10/)).toBeInTheDocument();
            expect(screen.getByText(/20/)).toBeInTheDocument();
        });
    });

    it('<퀴즈 만들기> 버튼을 누르면 퀴즈 생성 페이지로 이동한다', async () => {
        // given
        renderWithWrapper(<QuizPage />);
        const createLobbyButton = screen.getByLabelText('create-quiz');
        const user = userEvent.setup();

        // when
        await user.click(createLobbyButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith('/quizzes/create');
        });
    });
});
