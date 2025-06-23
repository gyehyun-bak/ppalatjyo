package ppalatjyo.server.game.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameEndedEvent {
    private Long gameId;
    private Long lobbyId;
}
