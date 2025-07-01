package ppalatjyo.server.domain.quiz.dto;

import lombok.Data;

@Data
public class AnswerUpdateRequestDto {
    private Long answerId;
    private String content;
}
