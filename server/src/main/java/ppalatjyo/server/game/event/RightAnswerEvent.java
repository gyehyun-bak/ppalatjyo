package ppalatjyo.server.game.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class RightAnswerEvent {
    private Long lobbyId;
    private Long gameId;
    private Long userId;
    private String userNickname;
    private Long messageId;
    private List<String> answers;

    public static RightAnswerEvent create(Game game, Message message, Question question) {
        return RightAnswerEvent.builder()
                .lobbyId(game.getLobby().getId())
                .gameId(game.getId())
                .userId(message.getUser().getId())
                .userNickname(message.getUser().getNickname())
                .messageId(message.getId())
                .answers(question.getAnswers().stream().map(Answer::getContent).toList())
                .build();
    }
}
