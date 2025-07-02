export interface FieldError {
    message: string;
    rejectedValue?: unknown;
}

export interface ErrorResponse {
    message: string;
    path: string;
    timestamp: string; // LocalDateTime -> ISO String
    data?: Record<string, FieldError>;
}
