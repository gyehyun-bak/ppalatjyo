package ppalatjyo.server.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewQuestionDto {
    private Long questionId;
    private String content;
}
