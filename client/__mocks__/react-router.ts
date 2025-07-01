// __mocks__/react-router.ts
import { vi, afterEach } from 'vitest';
export * from 'react-router';

// mock 함수
export const mockNavigate = vi.fn();
export const mockUseParams = vi.fn(() => ({}));
export const mockUseLocation = vi.fn(() => ({ pathname: '/' }));
export const mockUseSearchParams = vi.fn(() => [
    new URLSearchParams(),
    vi.fn(),
]);

// 상태 reset용 Set
export const reactRouterResetFns = new Set<() => void>();

reactRouterResetFns.add(() => {
    mockNavigate.mockReset();
    mockUseParams.mockReset();
    mockUseLocation.mockReset();
    mockUseSearchParams.mockReset();
});

// 실제 export와 함께 override
export const useNavigate = () => mockNavigate;
export const useParams = () => mockUseParams();
export const useLocation = () => mockUseLocation();
export const useSearchParams = () => mockUseSearchParams();

// 테스트 후 상태 초기화
afterEach(() => {
    reactRouterResetFns.forEach((reset) => reset());
});
