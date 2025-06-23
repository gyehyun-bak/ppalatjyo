package ppalatjyo.server.game.dto;

import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.game.event.GameStartedEvent;

import java.time.LocalDateTime;

@Data
@Builder
public class GameInfoDto {
    private Long gameId;
    private Integer minPerGame;
    private Integer secPerQuestion;
    private Integer totalQuestion;
    private LocalDateTime startedAt;

    public static GameInfoDto create(GameStartedEvent event) {
        return GameInfoDto.builder()
                .gameId(event.getGameId())
                .minPerGame(event.getMinPerGame())
                .secPerQuestion(event.getSecPerQuestion())
                .totalQuestion(event.getTotalQuestion())
                .startedAt(event.getStartedAt())
                .build();
    }
}
