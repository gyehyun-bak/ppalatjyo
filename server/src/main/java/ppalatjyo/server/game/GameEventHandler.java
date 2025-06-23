package ppalatjyo.server.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ppalatjyo.server.game.dto.GameEventDto;
import ppalatjyo.server.game.dto.GameInfoDto;
import ppalatjyo.server.game.dto.NewQuestionDto;
import ppalatjyo.server.game.event.*;
import ppalatjyo.server.global.scheduler.SchedulerService;
import ppalatjyo.server.global.websocket.MessageBrokerService;
import ppalatjyo.server.global.websocket.dto.PublicationDto;

/**
 * 발생한 GameEvent에 대해 핸들링합니다.
 * 메시지 발행과 타이머 스레드 생성을 관리합니다.
 * 기본적으로 모든 서비스 트랜잭션이 커밋되고 난 시점에 대해 다룹니다.
 */
@Component
@RequiredArgsConstructor
public class GameEventHandler {

    private final MessageBrokerService messageBrokerService;
    private final GameService gameService;
    private final SchedulerService schedulerService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGameStartedEvent(GameStartedEvent event) {
        Long gameId = event.getGameId();

        GameInfoDto eventDto = GameInfoDto.create(event);
        PublicationDto<GameInfoDto> dto = new PublicationDto<>(eventDto);

        messageBrokerService.publish(getDestination(event.getLobbyId()), dto);

        schedulerService.runAfterMinutes(event.getSecPerQuestion(),
                () -> gameService.end(gameId));

        NewQuestionDto newQuestionDto = new NewQuestionDto(event.getCurrentQuestionId(), event.getCurrentQuestionContent());

        publishNewQuestion(event.getLobbyId(), gameId, event.getSecPerQuestion(), newQuestionDto);
    }

    /**
     * 문제를 클라이언트에 제시하는 하는 이벤트 메시지를 발행합니다.
     * 이벤트를 기준으로 타임 아웃 카운트가 시작됩니다.
     * 타임 아웃 호출 시점에 동일한 Question인 경우 실제로 타임 아웃 처리됩니다.
     */
    public void publishNewQuestion(long gameId, long lobbyId, int secPerQuestion, NewQuestionDto newQuestionDto) {
        GameEventDto gameEventDto = GameEventDto.newQuestion(newQuestionDto);
        PublicationDto<GameEventDto> dto = new PublicationDto<>(gameEventDto);

        messageBrokerService.publish(getDestination(lobbyId), dto);

        schedulerService.runAfterSecondes(secPerQuestion,
                () -> gameService.timeOut(gameId, newQuestionDto.getQuestionId()));
    }

    /**
     * 다음 문제를 제시하는 이벤트에 대해 메시지를 발행합니다. 게임 시작 시 문제 발행과 로직이 동일합니다.
     * {@code publishNewQuestion()} 으로 위임합니다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNextQuestionEvent(NextQuestionEvent event) {
        NewQuestionDto newQuestionDto = new NewQuestionDto(event.getQuestionId(), event.getQuestionContent());
        publishNewQuestion(event.getGameId(), event.getLobbyId(), event.getSecPerQuestion(), newQuestionDto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGameEndedEvent(GameEndedEvent event) {
        Long gameId = event.getGameId();

        GameEventDto data = GameEventDto.ended(gameId);
        PublicationDto<GameEventDto> dto = new PublicationDto<>(data);

        messageBrokerService.publish(getDestination(event.getLobbyId()), dto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTimeOutEvent(TimeOutEvent event) {
        GameEventDto data = GameEventDto.timeOut(event.getGameId());
        PublicationDto<GameEventDto> dto = new PublicationDto<>(data);

        messageBrokerService.publish(getDestination(event.getLobbyId()), dto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRightAnswer(RightAnswerEvent event) {
        GameEventDto data = GameEventDto.rightAnswer(event.getGameId(), event.getUserId(), event.getNickname(), event.getMessageId());
        PublicationDto<GameEventDto> dto = new PublicationDto<>(data);

        messageBrokerService.publish(getDestination(event.getLobbyId()), dto);
    }

    private String getDestination(Long lobbyId) {
        String GAME_EVENT_DESTINATION_PREFIX = "/lobbies/";
        String GAME_EVENT_DESTINATION_SUFFIX = "/games/events";

        return GAME_EVENT_DESTINATION_PREFIX + lobbyId + GAME_EVENT_DESTINATION_SUFFIX;
    }
}
