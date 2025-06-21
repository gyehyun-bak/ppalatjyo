package ppalatjyo.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageSentEvent {
    private Long userId;
    private Long gameId;
    private Long messageId;
}
