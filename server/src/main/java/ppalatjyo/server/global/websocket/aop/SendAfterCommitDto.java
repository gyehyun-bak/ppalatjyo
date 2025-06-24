package ppalatjyo.server.global.websocket.aop;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendAfterCommitDto<T> {
    private String destination;
    private T data;
}
