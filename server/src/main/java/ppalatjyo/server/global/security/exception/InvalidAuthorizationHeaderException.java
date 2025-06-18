package ppalatjyo.server.global.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthorizationHeaderException extends AuthenticationException {
    public InvalidAuthorizationHeaderException(String message) {
        super(message);
    }
}
