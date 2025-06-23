package ppalatjyo.server.game.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresentQuestionEventDto {
    private Long questionId;
    private String content;
}
