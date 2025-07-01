package ppalatjyo.server.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import ppalatjyo.server.global.dto.error.ErrorResponseDto;

import java.io.IOException;

/**
 * AuthorizationFilter 에서 발생하는 AuthenticationException 혹은, Anonymous 유저에 대한 AccessDeniedException 을 처리합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.debug("Authentication Failed", authException);

        String errorMessage = "Authentication Failed.";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponseDto errorDto = ErrorResponseDto.commonError(errorMessage, request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), errorDto);
    }
}
