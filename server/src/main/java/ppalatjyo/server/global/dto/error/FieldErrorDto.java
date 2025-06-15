package ppalatjyo.server.global.dto.error;

import lombok.Data;

@Data
public class FieldErrorDto {
    private Object rejectedValue;
    private String message;
}
