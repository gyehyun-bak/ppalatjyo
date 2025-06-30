import axios from 'axios';

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
 */
api.interceptors.response.use(
    (response) => {
        // 응답 데이터를 그대로 반환
        return response;
    },
    (error) => {
        // 에러 응답 처리
        if (error.response && error.response.status === 401) {
            // 인증 오류 처리 로직 (예: 토큰 재발급)
            console.error('인증 오류 발생:', error);
        }
        return Promise.reject(error);
    }
);
