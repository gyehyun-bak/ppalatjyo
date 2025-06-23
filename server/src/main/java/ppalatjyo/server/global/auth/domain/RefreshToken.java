package ppalatjyo.server.global.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class RefreshToken {
    private Long userId;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public static RefreshToken create(Long userId, String refreshToken, int maxAgeInSeconds) {
        return RefreshToken.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(maxAgeInSeconds))
                .build();
    }
}
