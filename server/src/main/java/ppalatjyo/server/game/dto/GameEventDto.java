package ppalatjyo.server.game.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GameEventDto {
    private Long gameId;
    private GameEventType type;
    private Long userId; // 정답자 아이디
    private String nickname; // 정답자 닉네임
    private Long messageId; // 정답 메시지 아이디
    @Builder.Default
    private LocalDateTime publishedAt = LocalDateTime.now();

    public static GameEventDto started(Long gameId) {
        return GameEventDto.builder()
                .gameId(gameId)
                .type(GameEventType.GAME_STARTED)
                .build();
    }

    public static GameEventDto ended(Long gameId) {
        return GameEventDto.builder()
                .gameId(gameId)
                .type(GameEventType.GAME_ENDED)
                .build();
    }

    public static GameEventDto timeOut(Long gameId) {
        return GameEventDto.builder()
                .gameId(gameId)
                .type(GameEventType.TIME_OUT)
                .build();
    }

    public static GameEventDto rightAnswer(Long gameId, Long userId, String nickname, Long messageId) {
        return GameEventDto.builder()
                .gameId(gameId)
                .type(GameEventType.RIGHT_ANSWER)
                .userId(userId)
                .nickname(nickname)
                .messageId(messageId)
                .build();
    }
}
