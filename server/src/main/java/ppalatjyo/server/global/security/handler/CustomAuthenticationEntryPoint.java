package ppalatjyo.server.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import ppalatjyo.server.global.dto.ResponseDto;
import ppalatjyo.server.global.dto.error.ResponseErrorDto;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.debug("Authentication Failed", authException);

        String errorMessage = "Authentication Failed.";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ResponseErrorDto errorDto = ResponseErrorDto.commonError(errorMessage, request.getRequestURI());
        ResponseDto<Void> responseDto = ResponseDto.error(status, errorDto).getBody();

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), responseDto);
    }
}
