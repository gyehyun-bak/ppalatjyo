package ppalatjyo.server.global.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppalatjyo.server.global.auth.dto.GuestLoginRequestDto;
import ppalatjyo.server.global.auth.dto.SignUpAsGuestResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up/guest")
    public ResponseEntity<SignUpAsGuestResponseDto> guestLogin(@RequestBody GuestLoginRequestDto requestDto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.singUpAsGuest(requestDto.getNickname(), response));
    }

    @GetMapping("/tokens")
    public ResponseEntity<?> reissueTokens(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        return ResponseEntity.ok(authService.reissue(refreshToken, response));
    }
}
