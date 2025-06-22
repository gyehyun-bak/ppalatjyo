package ppalatjyo.server.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.domain.MessageType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MessageDto {
    private MessageType type;
    private Long userId;
    private String nickname;
    private String content;
    private LocalDateTime sentAt;

    public static MessageDto chatMessage(Message message) {
        return MessageDto.builder()
                .type(message.getType())
                .content(message.getContent())
                .userId(message.getUser().getId())
                .nickname(message.getUser().getNickname())
                .sentAt(message.getCreatedAt())
                .build();
    }

    public static MessageDto systemMessage(Message message) {
        return MessageDto.builder()
                .type(message.getType())
                .content(message.getContent())
                .sentAt(message.getCreatedAt())
                .build();
    }
}
