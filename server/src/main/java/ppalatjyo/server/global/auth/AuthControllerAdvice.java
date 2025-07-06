package ppalatjyo.server.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ppalatjyo.server.global.auth.exception.GitHubOAuthException;
import ppalatjyo.server.global.auth.exception.RefreshTokenException;
import ppalatjyo.server.global.error.ErrorResponseDto;

@RestControllerAdvice(assignableTypes = {AuthController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AuthControllerAdvice {

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleRefreshTokenException() {
        ErrorResponseDto errorDto = ErrorResponseDto.commonError("Refresh Failed", "/api/auth/tokens");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }

    @ExceptionHandler(GitHubOAuthException.class)
    public ResponseEntity<ErrorResponseDto> handleGitHubOAuthException(GitHubOAuthException ex, HttpServletRequest request) {
        ErrorResponseDto errorDto = ErrorResponseDto.commonError("Authenticating with GitHub failed. Authorization code has expired or invalid.", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
}
