import { HeroUIProvider, ToastProvider } from '@heroui/react';
import { Outlet } from 'react-router';
import { useNavigate, useHref } from 'react-router';

export default function RootLayout() {
    const navigate = useNavigate();

    return (
        <HeroUIProvider navigate={navigate} useHref={useHref}>
            <ToastProvider />
            <Outlet />
        </HeroUIProvider>
    );
}
