package ppalatjyo.server.global.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ppalatjyo.server.global.security.exception.JwtValidationException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();

        try {
            jwtTokenProvider.validateToken(token); // 여기서 예외가 발생하면 catch로 넘어감

            String userId = jwtTokenProvider.getUserIdFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId); // throws UsernameNotFoundException

            return JwtAuthenticationToken.authenticated(
                    userDetails,
                    token,
                    userDetails.getAuthorities());
        } catch (ExpiredJwtException e) { // JwtException을 AuthenticationException 으로 변환
            throw new JwtValidationException("Token has expired");
        } catch (MalformedJwtException e) {
            throw new JwtValidationException("Token is invalid");
        } catch (SignatureException e) {
            throw new JwtValidationException("Token signature exception");
        } catch (UnsupportedJwtException e) {
            throw new JwtValidationException("Unsupported token");
        } catch (IllegalArgumentException e) {
            throw new JwtValidationException("Invalid JWT token");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
