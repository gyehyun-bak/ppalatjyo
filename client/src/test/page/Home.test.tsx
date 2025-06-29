import { http, HttpResponse } from 'msw';
import { baseUrl, server } from '../../mocks/server';
import type { LobbiesResponseDto } from '../../types/api/lobby/LobbiesResponseDto';
import type { ResponseDto } from '../../types/api/ResponseDto';
import { renderWithWrapper } from '../utils/renderWithWrapper';
import Home from '../../page/Home';
import { waitFor, screen } from '@testing-library/dom';
import '@testing-library/jest-dom';

describe('Home', () => {
    it('로비 목록을 가져와 표시한다', async () => {
        // given
        server.use(
            http.get<never, never, ResponseDto<LobbiesResponseDto>>(
                baseUrl + '/lobbies',
                async () => {
                    return HttpResponse.json({
                        success: true,
                        status: 200,
                        data: {
                            lobbies: [
                                {
                                    id: 1,
                                    name: '로비 1',
                                    options: {
                                        maxUsers: 10,
                                        minPerGame: 2,
                                        secPerQuestion: 30,
                                    },
                                    quiz: {
                                        id: 1,
                                        title: '퀴즈 1',
                                    },
                                    host: { id: 1, nickname: '사용자1' },
                                },
                                {
                                    id: 2,
                                    name: '로비 2',
                                    options: {
                                        maxUsers: 5,
                                        minPerGame: 1,
                                        secPerQuestion: 20,
                                    },
                                    quiz: {
                                        id: 2,
                                        title: '퀴즈 2',
                                    },
                                    host: { id: 2, nickname: '사용자2' },
                                },
                            ],
                        },
                    });
                }
            )
        );

        // when
        renderWithWrapper(<Home />);

        // then
        await waitFor(() => {
            expect(screen.getByText('로비 1')).toBeInTheDocument();
            expect(screen.getByText('로비 2')).toBeInTheDocument();
            expect(screen.getByText('퀴즈 1')).toBeInTheDocument();
            expect(screen.getByText('퀴즈 2')).toBeInTheDocument();
            expect(screen.getByText('사용자1')).toBeInTheDocument();
            expect(screen.getByText('사용자2')).toBeInTheDocument();
        });
    });

    it('<새로고침> 버튼을 누르면 로비 목록을 새로고침한다', () => {});

    it('<로비 만들기> 버튼을 누르면 로비 만들기 페이지로 이동한다', () => {});
});
