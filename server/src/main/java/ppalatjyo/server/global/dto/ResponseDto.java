package ppalatjyo.server.global.dto;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ppalatjyo.server.global.dto.error.ResponseErrorDto;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseDto<T> {
    private boolean success;
    private int status;
    private String message;
    private T data;
    private ResponseErrorDto error;

    public static <T> ResponseEntity<ResponseDto<T>> ok(String message, T data) {
        ResponseDto<T> responseDto = ResponseDto.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto<Void>> ok(String message) {
        ResponseDto<Void> dto = ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message(message)
                .build();

        return ResponseEntity.ok(dto);
    }
}
