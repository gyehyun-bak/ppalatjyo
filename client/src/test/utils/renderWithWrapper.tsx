import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';
import { render, type RenderOptions } from '@testing-library/react';
import { MemoryRouter, type MemoryRouterProps } from 'react-router';

// 커스텀 옵션 타입
interface WrapperOptions {
    route?: string; // 초기 경로
    routerProps?: Partial<MemoryRouterProps>; // 전체 router 설정 (optional)
}

export function renderWithWrapper(
    ui: React.ReactElement,
    { route = '/', routerProps }: WrapperOptions = {},
    options?: Omit<RenderOptions, 'wrapper'>
) {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false } },
    });

    return render(
        <QueryClientProvider client={queryClient}>
            <MemoryRouter initialEntries={[route]} {...routerProps}>
                {ui}
            </MemoryRouter>
        </QueryClientProvider>,
        options
    );
}
