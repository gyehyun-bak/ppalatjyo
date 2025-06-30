import { Outlet } from 'react-router';
import { useAuthStore } from '../store/useAuthStore';
import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { getNewTokens } from '../api/auth.api';

/**
 * 인증 과정을 처리하는 레이아웃 컴포넌트입니다.
 * 토큰 재발급을 시도하고, 성공하면 인증 상태를 업데이트합니다.
 */
export default function AuthenticationLayout() {
    const { setAuthenticated } = useAuthStore();

    const { data, isSuccess } = useQuery({
        queryKey: ['tokens'],
        queryFn: getNewTokens,
        retry: false,
    });

    useEffect(() => {
        if (isSuccess && data.data) {
            setAuthenticated(true);
            localStorage.setItem('accessToken', data.data.accessToken);
        }
    }, [isSuccess, data, setAuthenticated]);

    return <Outlet />;
}
