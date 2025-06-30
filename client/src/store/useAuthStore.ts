import { create } from 'zustand';

interface AuthState {
    isAuthenticated: boolean;
    setAuthenticated: (authenticated: boolean) => void;
}

export const useAuthStore = create<AuthState>()((set) => ({
    isAuthenticated: false,
    setAuthenticated: (authenticated: boolean) =>
        set({ isAuthenticated: authenticated }),
}));
