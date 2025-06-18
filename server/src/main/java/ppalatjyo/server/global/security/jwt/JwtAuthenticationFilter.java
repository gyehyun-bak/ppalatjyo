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

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authenticationRequest = authenticationConverter.convert(request);
        if (authenticationRequest == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
        if (authenticationResult == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
        filterChain.doFilter(request, response);
    }
}
