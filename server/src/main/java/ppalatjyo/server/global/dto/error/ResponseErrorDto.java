package ppalatjyo.server.global.dto.error;

import lombok.*;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseErrorDto {
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, FieldErrorDto> data;

    public static ResponseErrorDto commonError(String message, String path) {
        return ResponseErrorDto.builder()
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ResponseErrorDto validationError(String path, Map<String, FieldErrorDto> data) {
        return ResponseErrorDto.builder()
                .message("Validation Error")
                .path(path)
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();
    }
}
