package ppalatjyo.server.game.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.game.domain.Game;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class NextQuestionEvent {
    private Long lobbyId;
    private Long gameId;
    private Long questionId;
    private String questionContent;
    private int secPerQuestion;

    public static NextQuestionEvent create(Game game) {
        return NextQuestionEvent.builder()
                .lobbyId(game.getLobby().getId())
                .gameId(game.getId())
                .questionId(game.getCurrentQuestion().getId())
                .questionContent(game.getCurrentQuestion().getContent())
                .secPerQuestion(game.getOptions().getSecPerQuestion())
                .build();
    }
}
