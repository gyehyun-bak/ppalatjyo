package ppalatjyo.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinAsMemberResponseDto {
    private String accessToken;
    private String refreshToken;
}
