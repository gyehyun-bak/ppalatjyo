package ppalatjyo.server.game.dto;

import lombok.Data;

@Data
public class SubmitAnswerRequestDto {
    private Long gameId;
    private Long lobbyId;
    private Long userGameId;
    private Long messageId;
}
