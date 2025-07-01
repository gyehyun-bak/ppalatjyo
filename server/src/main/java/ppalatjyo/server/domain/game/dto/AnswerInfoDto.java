package ppalatjyo.server.domain.game.dto;

import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.domain.message.domain.Message;
import ppalatjyo.server.domain.quiz.domain.Answer;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.user.domain.User;

import java.util.List;

@Data
@Builder
public class AnswerInfoDto {
    private List<String> answers;
    private Long correctUserId;
    private String correctUserNickname;
    private Long messageId;

    public static AnswerInfoDto create(Question question) {
        return AnswerInfoDto.builder()
                .answers(question.getAnswers().stream().map(Answer::getContent).toList())
                .build();
    }

    public static AnswerInfoDto create(Question question, User user, Message message) {
        return AnswerInfoDto.builder()
                .answers(question.getAnswers().stream().map(Answer::getContent).toList())
                .correctUserId(user.getId())
                .correctUserNickname(user.getNickname())
                .messageId(message.getId())
                .build();
    }
}
