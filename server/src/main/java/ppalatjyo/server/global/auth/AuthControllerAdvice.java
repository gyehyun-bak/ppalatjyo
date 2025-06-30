package ppalatjyo.server.global.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ppalatjyo.server.global.dto.ResponseDto;
import ppalatjyo.server.global.dto.error.ResponseErrorDto;

@RestControllerAdvice(assignableTypes = {AuthController.class})
public class AuthControllerAdvice {

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ResponseDto<Void>> handleRefreshTokenException() {
        ResponseErrorDto errorDto = ResponseErrorDto.commonError("Refresh Failed", "/api/auth/tokens");
        return ResponseDto.error(HttpStatus.FORBIDDEN, errorDto);
    }
}
