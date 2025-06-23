package ppalatjyo.server.game.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RightAnswerEvent {
    private Long gameId;
    private Long userId;
    private Long lobbyId;
    private String nickname;
    private Long messageId;
}
