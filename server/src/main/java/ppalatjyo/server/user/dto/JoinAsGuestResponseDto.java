package ppalatjyo.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinAsGuestResponseDto {
    private String accessToken;
    private String refreshToken;
}
