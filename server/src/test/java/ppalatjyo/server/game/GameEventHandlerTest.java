package ppalatjyo.server.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.game.event.GameEndedEvent;
import ppalatjyo.server.game.event.GameStartedEvent;
import ppalatjyo.server.game.event.RightAnswerEvent;
import ppalatjyo.server.game.event.TimeOutEvent;
import ppalatjyo.server.global.scheduler.SchedulerService;
import ppalatjyo.server.global.websocket.MessageBrokerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameEventHandlerTest {

    @Mock
    MessageBrokerService messageBrokerService;

    @Mock
    SchedulerService schedulerService;

    @InjectMocks
    GameEventHandler gameEventHandler;

    @Test
    @DisplayName("게임 시작 이벤트")
    void gameStartedEvent() {
        // given
        Long gameId = 1L;
        int minPerGame = 5;
        int secPerQuestion = 30;
        GameStartedEvent event = new GameStartedEvent(gameId, minPerGame, secPerQuestion);

        // when
        gameEventHandler.handleGameStartedEvent(event);

        // then
        verify(messageBrokerService).publish(any());
        verify(schedulerService).runAfterMinutes(anyInt(), any(Runnable.class));
        verify(schedulerService).runAfterSecondes(anyInt(), any(Runnable.class));
    }

    @Test
    @DisplayName("게임 종료 이벤트")
    void gameEndedEvent() {
        // given
        Long gameId = 1L;
        GameEndedEvent event = new GameEndedEvent(gameId);

        // when
        gameEventHandler.handleGameEndedEvent(event);

        // then
        verify(messageBrokerService).publish(any());
    }

    @Test
    @DisplayName("게임 타임업 이벤트")
    void gameTimeUp() {
        // given
        Long gameId = 1L;
        TimeOutEvent event = new TimeOutEvent(gameId);

        // when
        gameEventHandler.handleTimeOutEvent(event);

        // then
        verify(messageBrokerService).publish(any());
    }

    @Test
    @DisplayName("정답 이벤트")
    void rightAnswerEvent() {
        // given
        Long gameId = 1L;
        Long userId = 1L;
        String nickname = "winner";
        Long messageId = 1L;

        RightAnswerEvent event = new RightAnswerEvent(gameId, userId, nickname, messageId);

        // when
        gameEventHandler.handleRightAnswer(event);

        // then
        verify(messageBrokerService).publish(any());
    }
}