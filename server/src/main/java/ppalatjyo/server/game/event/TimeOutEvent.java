package ppalatjyo.server.game.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeOutEvent {
    private Long gameId;
}
