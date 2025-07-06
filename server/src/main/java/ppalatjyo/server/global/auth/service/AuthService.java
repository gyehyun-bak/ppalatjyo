package ppalatjyo.server.global.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.user.domain.OAuthProvider;
import ppalatjyo.server.global.auth.exception.RefreshTokenException;
import ppalatjyo.server.global.auth.domain.RefreshToken;
import ppalatjyo.server.global.auth.dto.JoinAsGuestResponseDto;
import ppalatjyo.server.global.auth.dto.JoinAsMemberByGitHubRequestDto;
import ppalatjyo.server.global.auth.dto.JoinAsMemberByGitHubResponseDto;
import ppalatjyo.server.global.auth.dto.TokenReissueResponseDto;
import ppalatjyo.server.global.auth.repository.RefreshTokenRepository;
import ppalatjyo.server.global.security.jwt.JwtTokenProvider;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.UserService;
import ppalatjyo.server.domain.user.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JoinAsGuestResponseDto joinAsGuest(String nickname, HttpServletResponse response) {
        long userId = userService.joinAsGuest(nickname);

        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = createAndSaveRefreshToken(userId);

        storeRefreshTokenInCookie(response, refreshToken);

        return new JoinAsGuestResponseDto(accessToken);
    }

    public JoinAsMemberByGitHubResponseDto joinAsMemberByGitHub(JoinAsMemberByGitHubRequestDto requestDto, HttpServletResponse response) {

        long userId = userService.joinAsMember(requestDto.getNickname(), "", OAuthProvider.GITHUB);

        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = createAndSaveRefreshToken(userId);
        storeRefreshTokenInCookie(response, refreshToken);

        return new JoinAsMemberByGitHubResponseDto(accessToken);
    }

    public TokenReissueResponseDto reissue(String oldRefreshToken, HttpServletResponse response) {
        if (oldRefreshToken == null || !jwtTokenProvider.validateToken(oldRefreshToken)) {
            throw new RefreshTokenException("Refresh Token is null or invalid");
        }

        long userId = Long.parseLong(jwtTokenProvider.getUserIdFromToken(oldRefreshToken));
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        deleteOldRefreshToken(oldRefreshToken);

        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = createAndSaveRefreshToken(userId);

        storeRefreshTokenInCookie(response, refreshToken);

        return new TokenReissueResponseDto(accessToken);
    }

    private void deleteOldRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
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
