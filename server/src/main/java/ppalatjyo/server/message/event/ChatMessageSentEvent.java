package ppalatjyo.server.message.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageSentEvent {
    private Long userId;
    private Long lobbyId;
    private Long messageId;
}
