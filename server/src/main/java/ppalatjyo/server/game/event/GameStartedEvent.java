package ppalatjyo.server.game.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameStartedEvent {
    private Long gameId;
    private Integer minPerGame;
    private Integer secPerQuestion;
}
