package ppalatjyo.server.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.game.event.*;
import ppalatjyo.server.global.scheduler.SchedulerService;
import ppalatjyo.server.global.websocket.MessageBrokerService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        GameStartedEvent event = mock(GameStartedEvent.class);

        // when
        gameEventHandler.handleGameStartedEvent(event);

        // then
        verify(messageBrokerService, times(2)).publish(anyString(), any());
        verify(schedulerService).runAfterMinutes(anyInt(), any(Runnable.class));
    }

    @Test
    @DisplayName("게임 종료 이벤트")
    void gameEndedEvent() {
        // given
        GameEndedEvent event = mock(GameEndedEvent.class);

        // when
        gameEventHandler.handleGameEndedEvent(event);

        // then
        verify(messageBrokerService).publish(anyString(), any());
    }

    @Test
    @DisplayName("게임 타임업 이벤트")
    void gameTimeUp() {
        // given
        TimeOutEvent event = mock(TimeOutEvent.class);

        // when
        gameEventHandler.handleTimeOutEvent(event);

        // then
        verify(messageBrokerService).publish(anyString(), any());
    }

    @Test
    @DisplayName("정답 이벤트")
    void rightAnswerEvent() {
        // given
        RightAnswerEvent event = mock(RightAnswerEvent.class);

        // when
        gameEventHandler.handleRightAnswer(event);

        // then
        verify(messageBrokerService).publish(anyString(), any());
    }

    @Test
    @DisplayName("NextQuestionEvent")
    void handleNextQuestionEvent() {
        // given
        NextQuestionEvent event = mock(NextQuestionEvent.class);

        // when
        gameEventHandler.handleNextQuestionEvent(event);

        // then
        verify(messageBrokerService).publish(anyString(), any());
    }
}