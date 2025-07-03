import axios, { type AxiosRequestConfig } from 'axios';
import { getNewTokens } from './auth.api';

/**
 * API 호출을 위한 BaseURL
 */
const baseURL = 'http://localhost:8080/api';

export const api = axios.create({
    baseURL: baseURL,
    timeout: 5000,
});

/**
 * Axios 인터셉터 설정
 * 요청에 대한 공통 처리를 수행합니다.
 */
api.interceptors.request.use(
    (config) => {
        // 요청 전에 토큰을 헤더에 추가
        const accessToken = localStorage.getItem('accessToken');
        if (accessToken) {
            config.headers['Authorization'] = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

/**
 * Axios 인터셉터 설정
 * 응답에 대한 공통 처리를 수행합니다.
 * 에러 응답에서 인증 오류를 잡아내어 토큰 재발급을 시도합니다.
 * 재발급 성공 시 원래 요청을 재시도합니다.
 * 재발급 실패 시 에러를 반환합니다.
 */
api.interceptors.response.use(
    (response) => {
        // 응답 데이터를 그대로 반환
        return response;
    },
    async (error) => {
        const originalRequest = error.config as AxiosRequestConfig & {
            _retry?: boolean;
        };

        // 토큰 만료로 인한 401 에러, 그리고 재시도 방지를 위해 flag 확인
        if (
            error.response?.status === 401 &&
            !originalRequest._retry &&
            originalRequest.url &&
            !originalRequest.url.includes('/auth/tokens')
        ) {
            originalRequest._retry = true;

            try {
                const tokenResponse = await getNewTokens(); // 재발급 요청
                const newAccessToken = tokenResponse.accessToken;
                localStorage.setItem('accessToken', newAccessToken);

                // Authorization 헤더 갱신 후 원래 요청 재시도
                if (!originalRequest.headers) {
                    originalRequest.headers = {};
                }
                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                return api(originalRequest);
            } catch (refreshError) {
                console.warn('Token refresh failed', refreshError);
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);
