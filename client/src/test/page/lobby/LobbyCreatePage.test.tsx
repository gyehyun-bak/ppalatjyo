import { screen, waitFor } from '@testing-library/dom';
import LobbyCreatePage from '../../../page/lobby/LobbyCreatePage';
import { useLobbyCreateStore } from '../../../store/useLobbyCreateStore';
import { renderWithWrapper } from '../../utils/renderWithWrapper';
import '@testing-library/jest-dom';
import { baseUrl, server } from '../../../mocks/server';
import { http, HttpResponse } from 'msw';
import type { ResponseDto } from '../../../types/api/ResponseDto';
import type { QuizResponseDto } from '../../../types/api/quiz/QuizResponseDto';

vi.mock('zustand');

describe('LobbyCreatePage', () => {
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
        renderWithWrapper(<LobbyCreatePage />);

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
            http.get<never, never, ResponseDto<QuizResponseDto>>(
                `${baseUrl}/quizzes/${quizId}`,
                () => {
                    return HttpResponse.json({
                        success: true,
                        status: 200,
                        data: {
                            id: quizId,
                            title: quizTitle,
                            authorNickname: '',
                            totalQuestions: 0,
                        },
                    });
                }
            )
        );

        // when
        renderWithWrapper(<LobbyCreatePage />, {
            route: `/lobbies/create?quizId=${quizId}`,
        });

        // then
        await waitFor(() => {
            expect(screen.getByText(quizTitle)).toBeInTheDocument();
        });
    });
    it('<퀴즈 선택하기> 버튼을 클릭하면, 현재 설정을 저장하고 "/quizzes"로 이동한다', () => {});
    it('모든 필수 입력 필드가 채워지지 않으면 <로비 만들기> 버튼이 비활성화된다', () => {});
    it('로비 만들기 버튼을 클릭하면, 로비 생성 API를 호출하고, 성공 시 "/lobbies/:lobbyId"로 이동한다', () => {});
});
