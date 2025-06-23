package ppalatjyo.server.global.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RefreshToken {
    private Long userId;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
