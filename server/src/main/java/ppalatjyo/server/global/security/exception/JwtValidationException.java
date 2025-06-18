package ppalatjyo.server.global.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtValidationException extends AuthenticationException {
    public JwtValidationException(String message) {
        super(message);
    }
}
