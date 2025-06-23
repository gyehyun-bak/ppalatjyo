package ppalatjyo.server.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String SECRET_KEY = "my-very-secret-key-that-should-be-at-least-256-bits-long!";
    private static final long ACCESS_TOKEN_MAX_AGE = 1000L * 60 * 60 * 24; // 24시간
    private static final long REFRESH_TOKEN_MAX_AGE = 1000L * 60 * 60 * 24 * 30; // 30일

    private final Key key;

    public JwtTokenProvider() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String createAccessToken(long userId) {
        return createToken(String.valueOf(userId), ACCESS_TOKEN_MAX_AGE);
    }

    public String createRefreshToken(long userId) {
        return createToken(String.valueOf(userId), REFRESH_TOKEN_MAX_AGE);
    }

    public String createToken(String userId, long maxAge) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + maxAge))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return null;
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public int getRefreshTokenMaxAgeInSeconds() {
        return (int) (REFRESH_TOKEN_MAX_AGE / 1000);
    }
}
