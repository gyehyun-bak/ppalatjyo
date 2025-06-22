package ppalatjyo.server.game;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ppalatjyo.server.game.dto.GameEventDto;
import ppalatjyo.server.game.event.GameEndedEvent;
import ppalatjyo.server.game.event.GameStartedEvent;
import ppalatjyo.server.game.event.RightAnswerEvent;
import ppalatjyo.server.game.event.TimeOutEvent;
import ppalatjyo.server.global.scheduler.SchedulerService;
import ppalatjyo.server.global.websocket.MessageBrokerService;
import ppalatjyo.server.global.websocket.dto.MessagePublicationDto;

/**
 * 발생한 GameEvent에 대해 핸들링합니다.
 * 메시지 발행과 타이머 스레드 생성을 관리합니다.
 * 기본적으로 모든 트랜잭션이 커밋되고 난 시점에 대해 다룹니다.
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

        GameEventDto data = GameEventDto.started(gameId);
        MessagePublicationDto<GameEventDto> dto = new MessagePublicationDto<>("/games/" + gameId + "/events", data);

        messageBrokerService.publish(dto);

        schedulerService.runAfterMinutes(event.getMinPerGame(),
                () -> gameService.end(gameId));
        schedulerService.runAfterSecondes(event.getSecPerQuestion(),
                () -> gameService.timeOut(gameId));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGameEndedEvent(GameEndedEvent gameEndedEvent) {
        Long gameId = gameEndedEvent.getGameId();

        GameEventDto data = GameEventDto.ended(gameId);
        MessagePublicationDto<GameEventDto> dto = new MessagePublicationDto<>("/games/" + gameId + "/events", data);

        messageBrokerService.publish(dto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTimeOutEvent(TimeOutEvent event) {
        GameEventDto data = GameEventDto.timeOut(event.getGameId());
        MessagePublicationDto<GameEventDto> dto = new MessagePublicationDto<>("/games/" + event.getGameId() + "/events", data);

        messageBrokerService.publish(dto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRightAnswer(RightAnswerEvent event) {
        GameEventDto data = GameEventDto.rightAnswer(event.getGameId(), event.getUserId(), event.getNickname(), event.getMessageId());
        MessagePublicationDto<GameEventDto> dto = new MessagePublicationDto<>("/games/" + event.getGameId() + "/events", data);

        messageBrokerService.publish(dto);
    }
}
