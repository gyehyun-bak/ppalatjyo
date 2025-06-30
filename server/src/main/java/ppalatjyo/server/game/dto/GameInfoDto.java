package ppalatjyo.server.game.dto;

import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.game.event.GameStartedEvent;

import java.time.LocalDateTime;

@Data
@Builder
public class GameInfoDto {
    private Long gameId;
    private Integer minPerGame;
    private Integer secPerQuestion;
    private Integer totalQuestions;
    private LocalDateTime startedAt;

    public static GameInfoDto create(GameStartedEvent event) {
        return GameInfoDto.builder()
                .gameId(event.getGameId())
                .minPerGame(event.getMinPerGame())
                .secPerQuestion(event.getSecPerQuestion())
                .totalQuestions(event.getTotalQuestions())
                .startedAt(event.getStartedAt())
                .build();
    }

    public static GameInfoDto create(Game game) {
        return GameInfoDto.builder()
                .gameId(game.getId())
                .minPerGame(game.getOptions().getMinPerGame())
                .secPerQuestion(game.getOptions().getSecPerQuestion())
                .totalQuestions(game.getQuiz().getQuestions().size())
                .startedAt(game.getStartedAt())
                .build();
    }
}
