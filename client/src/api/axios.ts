import axios from 'axios';

/**
 * API 호출을 위한 BaseURL
 */
const baseURL = 'http://localhost:8080/api';

export const api = axios.create({
    baseURL: baseURL,
    timeout: 5000,
});
