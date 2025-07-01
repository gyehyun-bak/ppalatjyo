package ppalatjyo.server.global.error;

import lombok.*;
import org.springframework.validation.FieldError;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldErrorDto {
    private Object rejectedValue;
    private String message;

    public static FieldErrorDto toDto(FieldError fieldError) {
        return FieldErrorDto.builder()
                .rejectedValue(fieldError.getRejectedValue())
                .message(fieldError.getDefaultMessage())
                .build();
    }
}
