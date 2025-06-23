package ppalatjyo.server.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.global.auth.domain.RefreshToken;
import ppalatjyo.server.global.auth.dto.SignUpAsGuestResponseDto;
import ppalatjyo.server.global.auth.dto.TokenReissueResponseDto;
import ppalatjyo.server.global.auth.repository.RefreshTokenRepository;
import ppalatjyo.server.global.security.exception.JwtValidationException;
import ppalatjyo.server.global.security.jwt.JwtTokenProvider;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.UserService;
import ppalatjyo.server.user.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public SignUpAsGuestResponseDto singUpAsGuest(String nickname, HttpServletResponse response) {
        long userId = userService.joinAsGuest(nickname);

        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = createAndSaveRefreshToken(userId);

        storeRefreshTokenInCookie(response, refreshToken);

        return new SignUpAsGuestResponseDto(accessToken);
    }

    public TokenReissueResponseDto reissue(String oldRefreshToken, HttpServletResponse response) {
        if (!jwtTokenProvider.validateToken(oldRefreshToken)) {
            throw new JwtValidationException("Invalid token");
        }

        long userId = Long.parseLong(jwtTokenProvider.getUserIdFromToken(oldRefreshToken));
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = createAndSaveRefreshToken(userId);

        storeRefreshTokenInCookie(response, refreshToken);

        return new TokenReissueResponseDto(accessToken);
    }

    private String createAndSaveRefreshToken(long userId) {
        String refreshTokenValue = jwtTokenProvider.createRefreshToken(userId);
        int maxAge = jwtTokenProvider.getRefreshTokenMaxAgeInSeconds();

        RefreshToken refreshToken = RefreshToken.create(userId, refreshTokenValue, maxAge);
        refreshTokenRepository.save(refreshToken);

        return refreshTokenValue;
    }

    private void storeRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(jwtTokenProvider.getRefreshTokenMaxAgeInSeconds());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
