import { Button, ButtonGroup } from '@heroui/react';
import { CircleUserRound, Home, MessageCircleQuestionMark } from 'lucide-react';

export default function BottomNavigation() {
    return (
        <footer className="absolute bottom-0 left-0 w-full z-50 bg-white border-t border-gray-200">
            <ButtonGroup className="w-full rounded-none">
                <BottomNavButton>
                    <Home />
                    <span className="text-xs">홈</span>
                </BottomNavButton>
                <BottomNavButton>
                    <MessageCircleQuestionMark />
                    <span className="text-xs">퀴즈</span>
                </BottomNavButton>
                <BottomNavButton>
                    <CircleUserRound />
                    <span className="text-xs">프로필</span>
                </BottomNavButton>
            </ButtonGroup>
        </footer>
    );
}

export function BottomNavButton({ children }: { children: React.ReactNode }) {
    return (
        <Button
            variant="light"
            radius="none"
            className="flex w-full py-3 flex-col h-fit"
        >
            {children}
        </Button>
    );
}
