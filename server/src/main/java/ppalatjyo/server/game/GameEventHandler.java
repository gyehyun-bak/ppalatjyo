package ppalatjyo.server.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.game.dto.GameEventDto;
import ppalatjyo.server.game.dto.GameStartedEventDto;
import ppalatjyo.server.game.dto.PresentQuestionEventDto;
import ppalatjyo.server.game.event.GameEndedEvent;
import ppalatjyo.server.game.event.GameStartedEvent;
import ppalatjyo.server.game.event.RightAnswerEvent;
import ppalatjyo.server.game.event.TimeOutEvent;
import ppalatjyo.server.game.exception.GameNotFoundException;
import ppalatjyo.server.global.scheduler.SchedulerService;
import ppalatjyo.server.global.websocket.MessageBrokerService;
import ppalatjyo.server.global.websocket.dto.PublicationDto;
import ppalatjyo.server.quiz.domain.Question;

/**
 * 발생한 GameEvent에 대해 핸들링합니다.
 * 메시지 발행과 타이머 스레드 생성을 관리합니다.
 * 기본적으로 모든 서비스 트랜잭션이 커밋되고 난 시점에 대해 다룹니다.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameEventHandler {

    private final GameRepository gameRepository;
    private final MessageBrokerService messageBrokerService;
    private final GameService gameService;
    private final SchedulerService schedulerService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGameStartedEvent(GameStartedEvent event) {
        Long gameId = event.getGameId();
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);

        GameStartedEventDto messageDto = GameStartedEventDto.create(game);
        PublicationDto<GameStartedEventDto> dto = new PublicationDto<>(messageDto);

        messageBrokerService.publish(getDestination(game.getLobby().getId()), dto);

        schedulerService.runAfterMinutes(game.getOptions().getSecPerQuestion(),
                () -> gameService.end(gameId));

        presentQuestion(game, game.getCurrentQuestion(), getDestination(game.getLobby().getId()));
    }

    /**
     * 문제를 클라이언트에 제시하는 하는 이벤트 메시지를 발급합니다.
     * 발급을 기준으로 타임 아웃 카운트가 시작됩니다.
     * 타임 아웃 호출 시점에 동일한 Question인 경우 실제로 타임 아웃 처리됩니다.
     */
    public void presentQuestion(Game game, Question question, String destination) {
        PresentQuestionEventDto messageDto = PresentQuestionEventDto.create(question);
        PublicationDto<PresentQuestionEventDto> dto = new PublicationDto<>(messageDto);

        messageBrokerService.publish(destination, dto);

        schedulerService.runAfterSecondes(game.getOptions().getSecPerQuestion(),
                () -> gameService.timeOut(game.getId(), question.getId()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGameEndedEvent(GameEndedEvent gameEndedEvent) {
        Long gameId = gameEndedEvent.getGameId();
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);

        GameEventDto data = GameEventDto.ended(gameId);
        PublicationDto<GameEventDto> dto = new PublicationDto<>(data);

        messageBrokerService.publish(getDestination(game.getLobby().getId()), dto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTimeOutEvent(TimeOutEvent event) {
        Game game = gameRepository.findById(event.getGameId()).orElseThrow(GameNotFoundException::new);
        GameEventDto data = GameEventDto.timeOut(event.getGameId());
        PublicationDto<GameEventDto> dto = new PublicationDto<>(data);

        messageBrokerService.publish(getDestination(game.getLobby().getId()), dto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRightAnswer(RightAnswerEvent event) {
        Game game = gameRepository.findById(event.getGameId()).orElseThrow(GameNotFoundException::new);
        GameEventDto data = GameEventDto.rightAnswer(event.getGameId(), event.getUserId(), event.getNickname(), event.getMessageId());
        PublicationDto<GameEventDto> dto = new PublicationDto<>(data);

        messageBrokerService.publish(getDestination(game.getLobby().getId()), dto);
    }

    private String getDestination(Long lobbyId) {
        String GAME_EVENT_DESTINATION_PREFIX = "/lobbies/";
        String GAME_EVENT_DESTINATION_SUFFIX = "/games/events";

        return GAME_EVENT_DESTINATION_PREFIX + lobbyId + GAME_EVENT_DESTINATION_SUFFIX;
    }
}
