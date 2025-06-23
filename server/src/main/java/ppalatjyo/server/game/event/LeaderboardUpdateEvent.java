package ppalatjyo.server.game.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ppalatjyo.server.game.dto.LeaderboardItemDto;

import java.util.List;

@Data
@AllArgsConstructor
public class LeaderboardUpdateEvent {
    private final Long lobbyId;
    private final List<LeaderboardItemDto> leaderboard;
}
