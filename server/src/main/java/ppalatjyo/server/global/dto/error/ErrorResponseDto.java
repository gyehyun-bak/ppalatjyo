package ppalatjyo.server.global.dto.error;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseDto {
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, FieldErrorDto> fieldErrors;

    public static ErrorResponseDto commonError(String message, String path) {
        return ErrorResponseDto.builder()
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponseDto validationError(String path, Map<String, FieldErrorDto> fieldErrors) {
        return ErrorResponseDto.builder()
                .message("Validation Error.")
                .path(path)
                .timestamp(LocalDateTime.now())
                .fieldErrors(fieldErrors)
                .build();
    }

    public static ResponseEntity<ErrorResponseDto> create(HttpStatus status, String message, String path) {
        ErrorResponseDto response = ErrorResponseDto.builder()
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
