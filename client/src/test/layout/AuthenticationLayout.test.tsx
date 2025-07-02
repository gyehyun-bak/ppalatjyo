import { waitFor } from '@testing-library/react';
import AuthenticationLayout from '../../layout/AuthenticationLayout';
import { renderWithWrapper } from '../utils/renderWithWrapper';
import { baseUrl, server } from '../../mocks/server';
import { http, HttpResponse } from 'msw';
import type { TokenReissueResponse } from '../../api/types/auth/TokenReissueResponse';
import '@testing-library/jest-dom';

describe('AuthenticationLayout', () => {
    beforeEach(() => {
        localStorage.clear(); // 테스트 간 상태 격리
    });

    it('토큰 재발급에 성공하면 accessToken을 저장하고 인증 상태를 true로 설정한다', async () => {
        // given: mock token API
        const mockAccessToken = 'new-access-token';
        server.use(
            http.get<never, never, TokenReissueResponse>(
                baseUrl + '/auth/tokens',
                () => {
                    return HttpResponse.json({
                        accessToken: mockAccessToken,
                    });
                }
            )
        );

        // when
        renderWithWrapper(<AuthenticationLayout />);

        // then
        await waitFor(() => {
            expect(localStorage.getItem('accessToken')).toBe(mockAccessToken);
        });
    });

    it('토큰 재발급에 실패하면 accessToken을 저장하지 않는다', async () => {
        // given
        server.use(
            http.get(baseUrl + '/auth/tokens', () => {
                return HttpResponse.json({}, { status: 403 });
            })
        );

        // when
        renderWithWrapper(<AuthenticationLayout />);

        // then
        await waitFor(() => {
            expect(localStorage.getItem('accessToken')).toBe(null);
        });
    });
});
