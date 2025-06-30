// useCounterStore.test.ts
import { describe, test, expect, vi } from 'vitest';
import { useCounterStore } from '../../store/useCountStore';

vi.mock('zustand'); // 👈 반드시 있어야 __mocks__/zustand.ts 적용됨

describe('useCounterStore', () => {
    test('increments count', () => {
        const store = useCounterStore.getState();
        expect(store.count).toBe(0);

        store.increase();
        expect(useCounterStore.getState().count).toBe(1);
    });

    test('resets between tests', () => {
        const store = useCounterStore.getState();
        // 이전 테스트에서 증가했어도 여기선 0이어야 함
        expect(store.count).toBe(0);
    });
});
