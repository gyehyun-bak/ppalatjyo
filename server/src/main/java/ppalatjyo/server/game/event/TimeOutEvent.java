package ppalatjyo.server.game.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class TimeOutEvent {
    private Long gameId;
    private Long lobbyId;
    private List<String> answers;

    public static TimeOutEvent create(Game game, Question question) {
        return TimeOutEvent.builder()
                .gameId(game.getId())
                .lobbyId(game.getLobby().getId())
                .answers(question.getAnswers().stream().map(Answer::getContent).toList())
                .build();
    }
}
