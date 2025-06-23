package ppalatjyo.server.game.dto;

import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.game.domain.Game;

import java.time.LocalDateTime;

@Data
@Builder
public class GameStartedEventDto {
    private Long gameId;
    private Integer minPerGame;
    private Integer secPerQuestion;
    private Integer totalQuestion;
    private LocalDateTime startedAt;

    public static GameStartedEventDto create(Game game) {
        return GameStartedEventDto.builder()
                .gameId(game.getId())
                .minPerGame(game.getOptions().getMinPerGame())
                .secPerQuestion(game.getOptions().getSecPerQuestion())
                .totalQuestion(game.getQuiz().getQuestions().size())
                .startedAt(game.getStartedAt())
                .build();
    }
}
