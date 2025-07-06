package ppalatjyo.server.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinAsGuestResponseDto {
    private String accessToken;
}
