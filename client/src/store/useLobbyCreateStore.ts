import { create } from 'zustand';

interface LobbyCreateState {
    name: string;
    setName: (name: string) => void;
    maxUsers: number;
    setMaxUsers: (maxUsers: number) => void;
    minPerGame: number;
    setMinPerGame: (minPerGame: number) => void;
    secPerQuestion: number;
    setSecPerQuestion: (secPerQuestion: number) => void;
}

export const useLobbyCreateStore = create<LobbyCreateState>((set) => ({
    name: '',
    setName: (name: string) => set({ name }),
    maxUsers: 10,
    setMaxUsers: (maxUsers: number) => set({ maxUsers }),
    minPerGame: 5,
    setMinPerGame: (minPerGame: number) => set({ minPerGame }),
    secPerQuestion: 60,
    setSecPerQuestion: (secPerQuestion: number) => set({ secPerQuestion }),
}));
