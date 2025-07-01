package ppalatjyo.server.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LeaderboardUpdateDto {
    private final List<LeaderboardItemDto> leaderboard;
}
