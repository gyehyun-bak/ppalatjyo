package ppalatjyo.server.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ppalatjyo.server.game.dto.AnswerInfoDto;
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

    private final int SECONDS_BEFORE_FIRST_QUESTION = 10;
    private final int SECONDS_BEFORE_NEXT_QUESTION = 5;

    private final MessageBrokerService messageBrokerService;
    private final GameService gameService;
    private final SchedulerService schedulerService;

    /**
     * 게임 시작 이벤트를 처리하는 메서드입니다.
     * <p>
     * 로 전달된 정보를 통해 다음 작업을 수행합니다:
     * <ul>
     *     <li>게임 시작 메시지를 메시지 브로커로 전송</li>
     *     <li>설정된 게임 제한 시간({@code minPerGame}) 후 게임 종료 스케줄 등록</li>
     *     <li>일정 대기 시간 후 첫 문항 출제</li>
     * </ul>
     *
     * @param event {@link GameStartedEvent}
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGameStartedEvent(GameStartedEvent event) {
        Long gameId = event.getGameId();

        GameInfoDto gameInfoDto = GameInfoDto.create(event);
        GameEventDto gameEventDto = GameEventDto.started(gameInfoDto);
        PublicationDto<GameEventDto> dto = new PublicationDto<>(gameEventDto);

        messageBrokerService.publish(getDestination(event.getLobbyId()), dto);

        schedulerService.runAfterMinutes(event.getMinPerGame(), () -> gameService.end(gameId));

        // 대기 시간을 두고 문제를 제시합니다.
        NewQuestionDto newQuestionDto = new NewQuestionDto(event.getFirstQuestionId(), event.getFirstQuestionContent());
        schedulerService.runAfterSecondes(SECONDS_BEFORE_FIRST_QUESTION,
                () -> publishNewQuestion(event.getLobbyId(), gameId, event.getSecPerQuestion(), newQuestionDto));
    }

    /**
     * <p>문제를 클라이언트에 제시하는 하는 이벤트 메시지를 발행합니다.
     * <p>이벤트를 기준으로 타임 아웃 카운트가 시작됩니다.
     * <p>타임 아웃 호출 시점에 동일한 Question인 경우 실제로 타임 아웃 처리됩니다.
     */
    public void publishNewQuestion(long gameId, long lobbyId, int secPerQuestion, NewQuestionDto newQuestionDto) {
        GameEventDto gameEventDto = GameEventDto.newQuestion(newQuestionDto);
        PublicationDto<GameEventDto> dto = new PublicationDto<>(gameEventDto);

        messageBrokerService.publish(getDestination(lobbyId), dto);

        schedulerService.runAfterSecondes(secPerQuestion,
                () -> gameService.timeOut(gameId, newQuestionDto.getQuestionId()));
    }

    /**
     * 다음 문제를 제시하는 이벤트에 대해 메시지를 발행합니다.
     * <p> 게임 시작 시 문제 발행과 로직이 동일합니다.
     * <p> 다음 문제 발행 전까지 정해진 시간만큼 대기합니다.
     * <p> {@code publishNewQuestion()} 으로 위임합니다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNextQuestionEvent(NextQuestionEvent event) {
        NewQuestionDto newQuestionDto = new NewQuestionDto(event.getQuestionId(), event.getQuestionContent());

        schedulerService.runAfterSecondes(SECONDS_BEFORE_NEXT_QUESTION,
                () -> publishNewQuestion(event.getGameId(), event.getLobbyId(), event.getSecPerQuestion(), newQuestionDto));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGameEndedEvent(GameEndedEvent event) {
        GameEventDto data = GameEventDto.ended();
        PublicationDto<GameEventDto> dto = new PublicationDto<>(data);

        messageBrokerService.publish(getDestination(event.getLobbyId()), dto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTimeOutEvent(TimeOutEvent event) {
        GameEventDto data = GameEventDto.timeOut(AnswerInfoDto.create(event));
        PublicationDto<GameEventDto> dto = new PublicationDto<>(data);

        messageBrokerService.publish(getDestination(event.getLobbyId()), dto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRightAnswer(RightAnswerEvent event) {
        GameEventDto data = GameEventDto.correct(AnswerInfoDto.create(event));
        PublicationDto<GameEventDto> dto = new PublicationDto<>(data);

        messageBrokerService.publish(getDestination(event.getLobbyId()), dto);
    }

    private String getDestination(Long lobbyId) {
        final String GAME_EVENT_DESTINATION_PREFIX = "/lobbies/";
        final String GAME_EVENT_DESTINATION_SUFFIX = "/games/events";

        return GAME_EVENT_DESTINATION_PREFIX + lobbyId + GAME_EVENT_DESTINATION_SUFFIX;
    }
}
