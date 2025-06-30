package ppalatjyo.server.game.dto;

import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.game.event.RightAnswerEvent;
import ppalatjyo.server.game.event.TimeOutEvent;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class AnswerInfoDto {
    private Long answerId;
    private List<String> answers;
    private Long correctUserId;
    private String correctUserNickname;
    private Long messageId;

    public static AnswerInfoDto create(Question question) {
        return AnswerInfoDto.builder()
                .answers(question.getAnswers().stream().map(Answer::getContent).toList())
                .build();
    }

    public static AnswerInfoDto create(TimeOutEvent event) {
        return AnswerInfoDto.builder()
                .answers(event.getAnswers())
                .build();
    }

    public static AnswerInfoDto create(RightAnswerEvent event) {
        return AnswerInfoDto.builder()
                .answers(event.getAnswers())
                .correctUserId(event.getUserId())
                .correctUserNickname(event.getUserNickname())
                .messageId(event.getMessageId())
                .build();
    }
}
