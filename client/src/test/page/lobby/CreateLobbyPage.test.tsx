import { screen, waitFor } from '@testing-library/dom';
import CreateLobbyPage from '../../../page/lobby/CreateLobbyPage';
import { useLobbyCreateStore } from '../../../store/useLobbyCreateStore';
import { renderWithWrapper } from '../../utils/renderWithWrapper';
import '@testing-library/jest-dom';
import { baseUrl, server } from '../../../mocks/server';
import { http, HttpResponse } from 'msw';
import type { QuizResponse } from '../../../api/types/quiz/QuizResponse';
import userEvent from '@testing-library/user-event';
import type { LobbyResponse } from '../../../api/types/lobby/LobbyResponse';
import { mockNavigate } from '../../../../__mocks__/react-router';

vi.mock('zustand');
vi.mock('react-router');

describe('CreateLobbyPage', () => {
    it('useLobbyStore에서 기존 설정을 불러온다', async () => {
        // given
        const prevName = '테스트 로비';
        const prevMaxUsers = 11;
        const prevMinPerGame = 22;
        const prevSecPerQuestion = 33;

        const { setName, setMaxUsers, setMinPerGame, setSecPerQuestion } =
            useLobbyCreateStore.getState();
        setName(prevName);
        setMaxUsers(prevMaxUsers);
        setMinPerGame(prevMinPerGame);
        setSecPerQuestion(prevSecPerQuestion);

        // when
        renderWithWrapper(<CreateLobbyPage />);

        // then
        expect(screen.getByLabelText('로비 이름')).toHaveValue(prevName);
        expect(screen.getByLabelText('최대 인원')).toHaveValue(
            String(prevMaxUsers)
        );
        expect(screen.getByLabelText('게임 시간 (분)')).toHaveValue(
            String(prevMinPerGame)
        );
        expect(screen.getByLabelText('문제당 제한 시간 (초)')).toHaveValue(
            String(prevSecPerQuestion)
        );
    });

    it('쿼리 파라미터에 quizId가 있으면, 퀴즈 정보를 불러와 선택한 퀴즈에 표시한다', async () => {
        // given
        const quizId = 123;
        const quizTitle = '테스트 퀴즈';

        server.use(
            http.get<never, never, QuizResponse>(
                `${baseUrl}/quizzes/${quizId}`,
                () => {
                    return HttpResponse.json({
                        id: quizId,
                        title: quizTitle,
                        authorNickname: '',
                        totalQuestions: 0,
                    });
                }
            )
        );

        // when
        renderWithWrapper(<CreateLobbyPage />, {
            route: `/lobbies/create?quizId=${quizId}`,
        });

        // then
        await waitFor(() => {
            expect(screen.getByText(quizTitle)).toBeInTheDocument();
        });
    });

    it('쿼리 파라미터에 quizId가 없으면, 퀴즈 선택하기 버튼이 표시된다', async () => {
        // when
        renderWithWrapper(<CreateLobbyPage />);

        // then
        await waitFor(() => {
            expect(screen.getByLabelText('select-quiz')).toBeInTheDocument();
        });
    });

    it('<퀴즈 선택하기> 버튼을 클릭하면, 현재 설정을 저장하고 "/quizzes"로 이동한다', async () => {
        // given
        const { setName, setMaxUsers, setMinPerGame, setSecPerQuestion } =
            useLobbyCreateStore.getState();
        setName('로비 이름');
        setMaxUsers(12);
        setMinPerGame(5);
        setSecPerQuestion(30);

        renderWithWrapper(<CreateLobbyPage />);

        const selectQuizButton = screen.getByLabelText('select-quiz');
        const user = userEvent.setup();

        // when
        await user.click(selectQuizButton);

        // then
        await waitFor(() => {
            const state = useLobbyCreateStore.getState();
            expect(state.name).toBe('로비 이름');
            expect(state.maxUsers).toBe(12);
            expect(state.minPerGame).toBe(5);
            expect(state.secPerQuestion).toBe(30);
            expect(mockNavigate).toHaveBeenCalledWith('/quizzes');
        });
    });

    it('모든 필수 입력 필드가 채워지지 않으면 <로비 만들기> 버튼이 비활성화된다', () => {
        // given
        renderWithWrapper(<CreateLobbyPage />);

        const createLobbyButton = screen.getByRole('button', {
            name: '로비 만들기',
        });
        const nameInput = screen.getByLabelText('로비 이름');
        const user = userEvent.setup();

        // when
        user.clear(nameInput);

        // then
        expect(createLobbyButton).toBeDisabled();
    });

    it('로비 만들기 버튼을 클릭하면, 로비 생성 API를 호출하고, 성공 시 "/lobbies/:lobbyId"로 이동한다', async () => {
        // given
        const quizId = 123;
        const lobbyId = 456;
        const lobbyName = '테스트 로비';
        const maxUsers = 10;
        const minPerGame = 5;
        const secPerQuestion = 30;

        server.use(
            http.post<never, never, LobbyResponse>(`${baseUrl}/lobbies`, () => {
                return HttpResponse.json({
                    id: lobbyId,
                    name: '',
                    currentUserCount: 0,
                    options: {
                        maxUsers: 0,
                        minPerGame: 0,
                        secPerQuestion: 0,
                    },
                    host: {
                        id: 0,
                        nickname: '',
                    },
                    quiz: {
                        id: 0,
                        title: '',
                        authorNickname: '',
                        totalQuestions: 0,
                    },
                });
            }),
            http.get<never, never, QuizResponse>(
                `${baseUrl}/quizzes/${quizId}`,
                () => {
                    return HttpResponse.json({
                        id: quizId,
                        title: '테스트 퀴즈',
                        authorNickname: '',
                        totalQuestions: 0,
                    });
                }
            )
        );

        renderWithWrapper(<CreateLobbyPage />, {
            route: `/lobbies/create?quizId=${quizId}`,
        });
        const createLobbyButton = screen.getByRole('button', {
            name: '로비 만들기',
        });
        const nameInput = screen.getByLabelText('로비 이름');
        const maxUsersInput = screen.getByLabelText('최대 인원');
        const minPerGameInput = screen.getByLabelText('게임 시간 (분)');
        const secPerQuestionInput =
            screen.getByLabelText('문제당 제한 시간 (초)');
        const user = userEvent.setup();

        // when
        await user.type(nameInput, lobbyName);
        await user.type(maxUsersInput, String(maxUsers));
        await user.type(minPerGameInput, String(minPerGame));
        await user.type(secPerQuestionInput, String(secPerQuestion));
        await user.click(createLobbyButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith(`/lobbies/${lobbyId}`);
        });
    });
});
