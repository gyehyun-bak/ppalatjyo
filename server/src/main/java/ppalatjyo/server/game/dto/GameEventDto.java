package ppalatjyo.server.game.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * {@code /lobbies/{lobbyId}/game/events} 에 발행되는 이벤트 메시지 DTO. 각 타입에 따라 포함되는 데이터가 구분됩니다.
 */
@Data
@Builder
public class GameEventDto {
    private GameEventType type;

    private GameInfoDto gameInfo;
    private NewQuestionDto newQuestion;
    private AnswerInfoDto answerInfo;

    private Long gameId;

    private Integer minPerGame;
    private Integer secPerQuestion;
    private Integer totalQuestion;


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

    public static GameEventDto newQuestion(NewQuestionDto newQuestion) {
        return GameEventDto.builder()
                .type(GameEventType.NEW_QUESTION)
                .newQuestion(newQuestion)
                .publishedAt(LocalDateTime.now())
                .build();
    }
}
