package ppalatjyo.server.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import ppalatjyo.server.global.security.exception.InvalidAuthorizationHeaderException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements AuthenticationConverter {

    @Override
    public JwtAuthenticationToken convert(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            throw new InvalidAuthorizationHeaderException("No authorization header found");
        } else if (!authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidAuthorizationHeaderException("Authorization header should start with 'Bearer'");
        }

        String token = authorizationHeader.substring(7);
        if (token.isEmpty()) {
            throw new InvalidAuthorizationHeaderException("Token is not provided after 'Bearer'");
        }

        return JwtAuthenticationToken.unauthenticated(token);
    }
}
