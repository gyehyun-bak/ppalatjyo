package ppalatjyo.server.global.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppalatjyo.server.global.auth.dto.JoinAsGuestRequestDto;
import ppalatjyo.server.global.auth.dto.JoinAsGuestResponseDto;
import ppalatjyo.server.global.auth.dto.JoinAsMemberByGitHubRequestDto;
import ppalatjyo.server.global.auth.dto.JoinAsMemberByGitHubResponseDto;
import ppalatjyo.server.global.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join/guest")
    public ResponseEntity<JoinAsGuestResponseDto> guestLogin(@RequestBody JoinAsGuestRequestDto requestDto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.joinAsGuest(requestDto.getNickname(), response));
    }

    @PostMapping("/join/github")
    public ResponseEntity<JoinAsMemberByGitHubResponseDto> joinAsMemberByGitHub(@RequestBody JoinAsMemberByGitHubRequestDto requestDto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.joinAsMemberByGitHub(requestDto, response));
    }

    @GetMapping("/tokens")
    public ResponseEntity<?> reissueTokens(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        return ResponseEntity.ok(authService.reissue(refreshToken, response));
    }
}
