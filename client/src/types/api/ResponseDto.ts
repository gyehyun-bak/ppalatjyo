import type { ResponseErrorDto } from './ResponseErrorDto';

export interface ResponseDto<T> {
    success: boolean;
    status: number;
    data?: T;
    error?: ResponseErrorDto;
}
