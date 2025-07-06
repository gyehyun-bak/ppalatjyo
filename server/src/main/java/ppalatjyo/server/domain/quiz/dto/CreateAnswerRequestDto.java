package ppalatjyo.server.domain.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAnswerRequestDto {
    @NotNull(message = "questionId는 필수입니다.")
    private Long questionId;
    @NotBlank(message = "content는 빈 값일 수 없습니다.")
    private String content;
}
