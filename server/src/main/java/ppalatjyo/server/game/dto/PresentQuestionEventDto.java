package ppalatjyo.server.game.dto;

import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.quiz.domain.Question;

@Data
@Builder
public class PresentQuestionEventDto {
    private Long questionId;
    private String content;

    public static PresentQuestionEventDto create(Question question) {
        return PresentQuestionEventDto.builder()
                .questionId(question.getId())
                .content(question.getContent())
                .build();
    }
}
