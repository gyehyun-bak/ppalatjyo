// __mocks__/@heroui/react.ts
import { vi, afterEach } from 'vitest';
export * from '@heroui/react';

export const mockAddToast = vi.fn();

// 실제 모듈 가져와 일부만 오버라이드
const actual = await vi.importActual<typeof import('@heroui/react')>(
    '@heroui/react'
);

export default {
    ...actual,
    addToast: mockAddToast,
};

// 상태 초기화 (선택사항이지만 권장)
afterEach(() => {
    mockAddToast.mockReset();
});
