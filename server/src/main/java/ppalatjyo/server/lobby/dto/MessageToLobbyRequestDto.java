package ppalatjyo.server.lobby.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageToLobbyRequestDto {
    private Long userId;
    private Long lobbyId;
    private String content;
}
