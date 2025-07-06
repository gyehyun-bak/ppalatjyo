package ppalatjyo.server.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinAsMemberByGitHubRequestDto {
    private Long userId;
    private String nickname;
    private String code;
}
