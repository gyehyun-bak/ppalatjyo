package ppalatjyo.server.message.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SystemMessageSentEvent {
    private Long lobbyId;
    private Long messageId;
}
