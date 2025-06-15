package ppalatjyo.server.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            Authentication authenticationResult = attemptAuthentication(request, response);
            if (authenticationResult == null) {
                return;
            }
            successfulAuthentication(request, response, filterChain, authenticationResult);
        } catch (Exception ex) {
            unsuccessfulAuthentication(request, response, ex);
        }

        filterChain.doFilter(request,response);
    }

    private Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = resolveToken(request);
        return null;
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authenticationResult) {

    }

    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Exception ex) {

    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}
