package ppalatjyo.server.domain.userlobby.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class JoinLobbyRequestDto {
    @NotNull
    private final Long userId;
    @NotNull
    private final Long lobbyId;
    private String password;
}
