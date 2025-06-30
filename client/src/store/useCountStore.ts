import { create } from 'zustand';

interface CounterState {
    count: number;
    increase: () => void;
    reset: () => void;
}

export const useCounterStore = create<CounterState>((set) => ({
    count: 0,
    increase: () => set((state) => ({ count: state.count + 1 })),
    reset: () => set({ count: 0 }),
}));
