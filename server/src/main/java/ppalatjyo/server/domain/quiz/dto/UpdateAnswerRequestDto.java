package ppalatjyo.server.domain.quiz.dto;

import lombok.Data;

@Data
public class UpdateAnswerRequestDto {
    private Long answerId;
    private String content;
}
