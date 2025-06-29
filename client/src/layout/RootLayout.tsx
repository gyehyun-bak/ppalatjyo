import { HeroUIProvider, ToastProvider } from '@heroui/react';
import { Outlet } from 'react-router';
import { useNavigate, useHref } from 'react-router';

export default function RootLayout() {
    const navigate = useNavigate();

    return (
        <HeroUIProvider navigate={navigate} useHref={useHref}>
            <ToastProvider />
            <div
                className="flex min-w-screen justify-center bg-neutral-200"
                style={{ height: '100dvh' }}
            >
                <div className="flex overflow-hidden h-full w-full max-w-md bg-white relative">
                    <Outlet />
                </div>
            </div>
        </HeroUIProvider>
    );
}
