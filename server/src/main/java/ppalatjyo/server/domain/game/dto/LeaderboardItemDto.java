package ppalatjyo.server.domain.game.dto;

import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.domain.usergame.UserGame;

@Data
@Builder
public class LeaderboardItemDto {
    private Long userId;
    private String username;
    private Integer score;

    public static LeaderboardItemDto create(UserGame userGame) {
        return LeaderboardItemDto.builder()
                .userId(userGame.getUser().getId())
                .username(userGame.getUser().getNickname())
                .score(userGame.getScore())
                .build();
    }
}
