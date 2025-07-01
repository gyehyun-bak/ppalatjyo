package ppalatjyo.server.domain.userlobby.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLeftLobbyEvent {
    private Long userId;
    private Long lobbyId;
}
