package ppalatjyo.server.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.UserService;
import ppalatjyo.server.domain.user.domain.OAuthProvider;
import ppalatjyo.server.global.auth.domain.RefreshToken;
import ppalatjyo.server.global.auth.dto.JoinAsGuestResponseDto;
import ppalatjyo.server.global.auth.dto.JoinAsMemberByGitHubRequestDto;
import ppalatjyo.server.global.auth.dto.JoinAsMemberByGitHubResponseDto;
import ppalatjyo.server.global.auth.dto.TokenReissueResponseDto;
import ppalatjyo.server.global.auth.repository.RefreshTokenRepository;
import ppalatjyo.server.global.auth.service.AuthService;
import ppalatjyo.server.global.auth.service.GitHubOAuthService;
import ppalatjyo.server.global.security.jwt.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GitHubOAuthService gitHubOAuthService;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("게스트로 가입")
    void signUpAsGuest() {
        // given
        String nickname = "nickname";
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(userService.joinAsGuest(anyString())).thenReturn(1L);
        when(jwtTokenProvider.createAccessToken(anyLong())).thenReturn("accessToken");

        // when
        JoinAsGuestResponseDto responseDto = authService.joinAsGuest(nickname, response);

        // then
        verify(userService).joinAsGuest(nickname);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getAccessToken()).isNotNull();
    }

    @Test
    @DisplayName("토큰 재발급")
    void reissueTokens() {
        // given
        long userId = 1L;
        String refreshTokenString = "refreshToken";
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(refreshTokenString)).thenReturn(String.valueOf(userId));
        when(userRepository.existsById(userId)).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(anyLong())).thenReturn("accessToken");
        when(jwtTokenProvider.createRefreshToken(anyLong())).thenReturn("refreshToken");

        // when
        TokenReissueResponseDto responseDto = authService.reissue(refreshTokenString, response);

        // then
        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        verify(refreshTokenRepository).deleteByRefreshToken(anyString());

        RefreshToken refreshToken = captor.getValue();
        assertThat(refreshToken).isNotNull();
        verify(response).addCookie(any(Cookie.class));

        assertThat(responseDto.getAccessToken()).isNotNull();
    }

    @Test
    @DisplayName("멤버로 가입 - GitHub")
    void joinAsMemberByGitHub() {
        // given
        String nickname = "nickname";
        String code = "code";
        String email = "email";
        HttpServletResponse response = mock(HttpServletResponse.class);

        JoinAsMemberByGitHubRequestDto requestDto = new JoinAsMemberByGitHubRequestDto();
        requestDto.setNickname(nickname);
        requestDto.setCode(code);

        when(jwtTokenProvider.createAccessToken(anyLong())).thenReturn("accessToken");
        when(jwtTokenProvider.createRefreshToken(anyLong())).thenReturn("refreshToken");
        when(gitHubOAuthService.getEmailFromCode(code)).thenReturn(email);

        // when
        JoinAsMemberByGitHubResponseDto responseDto = authService.joinAsMemberByGitHub(requestDto, response);

        // then
        verify(userService).joinAsMember(nickname, email, OAuthProvider.GITHUB);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        assertThat(responseDto.getAccessToken()).isNotNull();
    }
}