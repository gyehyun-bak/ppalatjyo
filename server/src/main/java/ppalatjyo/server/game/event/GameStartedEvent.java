package ppalatjyo.server.game.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ppalatjyo.server.game.domain.Game;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameStartedEvent {
    private Long gameId;
    private Long lobbyId;
    private Integer minPerGame;
    private Integer secPerQuestion;
    private Integer totalQuestions;
    private LocalDateTime startedAt;
    private Long firstQuestionId;
    private String firstQuestionContent;

    public static GameStartedEvent create(Game game) {
        return GameStartedEvent.builder()
                .gameId(game.getId())
                .lobbyId(game.getLobby().getId())
                .minPerGame(game.getOptions().getMinPerGame())
                .secPerQuestion(game.getOptions().getSecPerQuestion())
                .totalQuestions(game.getQuiz().getQuestions().size())
                .startedAt(game.getStartedAt())
                .firstQuestionId(game.getCurrentQuestion().getId())
                .firstQuestionContent(game.getCurrentQuestion().getContent())
                .build();
    }
}
