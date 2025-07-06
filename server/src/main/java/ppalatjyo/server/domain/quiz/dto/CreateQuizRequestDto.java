package ppalatjyo.server.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ppalatjyo.server.domain.quiz.domain.QuizVisibility;

@Data
@AllArgsConstructor
public class CreateQuizRequestDto {
    private Long userId;
    private String title;
    private String description;
    private QuizVisibility visibility;
}
