package ppalatjyo.server.global.dto.error;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ResponseErrorDto {
    private ErrorType type;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, FieldErrorDto> data;
}
