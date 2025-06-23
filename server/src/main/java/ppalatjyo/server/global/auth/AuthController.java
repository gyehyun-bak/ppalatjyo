package ppalatjyo.server.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppalatjyo.server.global.dto.ResponseDto;
import ppalatjyo.server.global.auth.dto.GuestLoginRequestDto;
import ppalatjyo.server.global.auth.dto.SignUpAsGuestResponseDto;
import ppalatjyo.server.global.dto.error.ResponseErrorDto;
import ppalatjyo.server.global.security.jwt.JwtTokenProvider;
import ppalatjyo.server.user.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up/guest")
    public ResponseEntity<ResponseDto<SignUpAsGuestResponseDto>> guestLogin(@RequestBody GuestLoginRequestDto requestDto, HttpServletResponse response) {
        long userId = userService.joinAsGuest(requestDto.getNickname());
        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);
        storeRefreshTokenInCookie(response, refreshToken, jwtTokenProvider.getRefreshTokenMaxAgeInSeconds());
        return ResponseDto.ok(new SignUpAsGuestResponseDto(accessToken));
    }

    @GetMapping("/tokens")
    public ResponseEntity<?> reissueTokens(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            ResponseErrorDto errorDto = ResponseErrorDto.commonError("RefreshToken Not Found", "/api/auth/tokens");
            return ResponseDto.error(HttpStatus.UNAUTHORIZED, errorDto);
        }

        return ResponseDto.ok(authService.reissue(refreshToken, response));
    }

    private void storeRefreshTokenInCookie(HttpServletResponse response, String refreshToken, int maxAgeInSeconds) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
