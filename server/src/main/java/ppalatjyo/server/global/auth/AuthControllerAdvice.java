package ppalatjyo.server.global.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ppalatjyo.server.global.dto.error.ErrorResponseDto;

@RestControllerAdvice(assignableTypes = {AuthController.class})
public class AuthControllerAdvice {

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleRefreshTokenException() {
        ErrorResponseDto errorDto = ErrorResponseDto.commonError("Refresh Failed", "/api/auth/tokens");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }
}
