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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up/guest")
    public ResponseEntity<ResponseDto<SignUpAsGuestResponseDto>> guestLogin(@RequestBody GuestLoginRequestDto requestDto, HttpServletResponse response) {
        return ResponseDto.ok(authService.singUpAsGuest(requestDto.getNickname(), response));
    }

    @GetMapping("/tokens")
    public ResponseEntity<?> reissueTokens(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            ResponseErrorDto errorDto = ResponseErrorDto.commonError("RefreshToken Not Found", "/api/auth/tokens");
            return ResponseDto.error(HttpStatus.UNAUTHORIZED, errorDto);
        }

        return ResponseDto.ok(authService.reissue(refreshToken, response));
    }
}
