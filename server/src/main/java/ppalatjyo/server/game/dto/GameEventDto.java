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

    @Builder.Default
    private LocalDateTime publishedAt = LocalDateTime.now();

    public static GameEventDto started(GameInfoDto gameInfo) {
        return GameEventDto.builder()
                .gameInfo(gameInfo)
                .type(GameEventType.GAME_STARTED)
                .build();
    }

    public static GameEventDto ended() {
        return GameEventDto.builder()
                .type(GameEventType.GAME_ENDED)
                .build();
    }

    public static GameEventDto timeOut(AnswerInfoDto answerInfo) {
        return GameEventDto.builder()
                .answerInfo(answerInfo)
                .type(GameEventType.TIME_OUT)
                .build();
    }

    public static GameEventDto correct(AnswerInfoDto answerInfo) {
        return GameEventDto.builder()
                .type(GameEventType.CORRECT)
                .answerInfo(answerInfo)
                .build();
    }

    public static GameEventDto newQuestion(NewQuestionDto newQuestion) {
        return GameEventDto.builder()
                .type(GameEventType.NEW_QUESTION)
                .newQuestion(newQuestion)
                .build();
    }
}
