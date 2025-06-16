package ppalatjyo.server.global.validation;

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
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class MethodArgumentNotValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Void>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, FieldErrorDto> errorMap = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldErrorDto::toDto
                ));

        ResponseErrorDto responseErrorDto = ResponseErrorDto.validationError(request.getRequestURI(), errorMap);

        return ResponseDto.error(HttpStatus.BAD_REQUEST, "Validation Error", responseErrorDto);
    }
}
