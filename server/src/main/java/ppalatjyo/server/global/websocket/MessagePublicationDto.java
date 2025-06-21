package ppalatjyo.server.global.websocket;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessagePublicationDto<T> {
    @NotNull
    private String destination;
    @NotNull
    private T data;
}
