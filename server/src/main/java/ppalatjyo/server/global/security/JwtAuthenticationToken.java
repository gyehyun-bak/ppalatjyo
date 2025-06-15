package ppalatjyo.server.global.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import javax.security.auth.Subject;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    private final Object credentials;

    public JwtAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
