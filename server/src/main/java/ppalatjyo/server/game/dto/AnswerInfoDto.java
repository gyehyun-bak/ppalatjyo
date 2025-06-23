package ppalatjyo.server.game.dto;

import lombok.Data;

@Data
public class AnswerInfoDto {
    private String answer;
    private Long correctUserId;
    private String correctUserNickname;
    private Long messageId;
}
