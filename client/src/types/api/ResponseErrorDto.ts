export interface FieldErrorDto {
    message: string;
    rejectedValue?: unknown;
}

export interface ResponseErrorDto {
    message: string;
    path: string;
    timestamp: string; // LocalDateTime -> ISO String
    data?: Record<string, FieldErrorDto>;
}
