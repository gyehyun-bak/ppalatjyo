package ppalatjyo.server.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ppalatjyo.server.global.dto.ResponseDto;
import ppalatjyo.server.global.dto.error.ResponseErrorDto;
import ppalatjyo.server.global.security.PermittedUrlChecker;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final PermittedUrlChecker permittedUrlChecker;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (permittedUrlChecker.isPermitted(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authenticationRequest = authenticationConverter.convert(request);
            Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);

            onSuccessfulAuthentication(request, response, authenticationResult);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            onUnsuccessfulAuthentication(request, response, e);
        }
    }

    private void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authenticationResult) {
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
    }

    private void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        log.info("Authentication Failed", ex);

        String message = "Authentication Failed";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ResponseErrorDto errorDto = ResponseErrorDto.commonError(ex.getMessage(), request.getRequestURI());
        ResponseDto<Void> responseDto = ResponseDto.error(status, message, errorDto).getBody();

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), responseDto);
    }
}
