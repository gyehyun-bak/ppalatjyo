package ppalatjyo.server.global.dto;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ppalatjyo.server.global.dto.error.ResponseErrorDto;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseDto<T> {
    private final boolean success;
    private final int status;
    private T data;
    private ResponseErrorDto error;

    public static <T> ResponseEntity<ResponseDto<T>> ok(T data) {
        ResponseDto<T> responseDto = ResponseDto.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .data(data)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto<Void>> ok() {
        ResponseDto<Void> dto = ResponseDto.<Void>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(dto);
    }

    public static ResponseEntity<ResponseDto<Void>> error(HttpStatus status, ResponseErrorDto errorDto) {
        ResponseDto<Void> dto = ResponseDto.<Void>builder()
                .success(false)
                .status(status.value())
                .error(errorDto)
                .build();

        return ResponseEntity.status(status).body(dto);
    }
}
