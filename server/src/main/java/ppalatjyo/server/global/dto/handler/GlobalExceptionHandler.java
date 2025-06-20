package ppalatjyo.server.global.dto.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ppalatjyo.server.global.dto.ResponseDto;
import ppalatjyo.server.global.dto.error.FieldErrorDto;
import ppalatjyo.server.global.dto.error.ResponseErrorDto;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * MethodArgumentNotValidException 발생 시 400 BAD_REQUEST로 응답하는 핸들러.
     * 유효성 검사 실패 시 발생하는 예외로, FieldError 정보를 통해
     * 어떤 필드가 어떻게 유효하지 않은지를 추출하여 반환합니다.
     *
     * @param ex      발생된 예외
     * @param request 현재 요청 정보
     * @return 400 상태를 갖는 표준 ResponseDto
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Void>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, FieldErrorDto> errorMap = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldErrorDto::toDto
                ));

        ResponseErrorDto responseErrorDto = ResponseErrorDto.validationError(request.getRequestURI(), errorMap);

        return ResponseDto.error(HttpStatus.BAD_REQUEST, responseErrorDto);
    }

    /**
     * NoSuchElementException 발생 시 404 NOT_FOUND로 응답하는 핸들러. NoSuchElementException을 상속받는 각 클래스에 에러 메시지를 포함해두어 그것을 사용합니다.
     *
     * <p> 예: ex.getMessage() -> "User Not Found"
     *
     * @param ex      발생된 예외
     * @param request 현재 요청 정보
     * @return 404 상태를 갖는 표준 ResponseDto
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseDto<Void>> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        ResponseErrorDto errorDto = ResponseErrorDto.commonError(ex.getMessage(), request.getRequestURI());
        return ResponseDto.error(HttpStatus.NOT_FOUND, errorDto);
    }

    /**
     * 기타 예외 발생 시 500 Internal Server Error로 응답하는 핸들러. 정확히 어떤 에러가 발생했는지 모르기 때문에 예외 메시지를 드러내지 않습니다. 대신 로그로 남깁니다.
     *
     * @param ex      발생된 예외
     * @param request 현재 요청 정보
     * @return 500 상태를 갖는 표준 ResponseDto
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleException(Exception ex, HttpServletRequest request) {
        log.error(ex.getMessage(), ex);
        ResponseErrorDto errorDto = ResponseErrorDto.commonError("Server Error", request.getRequestURI());
        return ResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR, errorDto);
    }
}
