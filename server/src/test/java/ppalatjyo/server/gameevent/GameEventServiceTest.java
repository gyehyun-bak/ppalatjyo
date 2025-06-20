package ppalatjyo.server.gameevent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.game.GameRepository;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.gameevent.domain.GameEvent;
import ppalatjyo.server.gameevent.domain.GameEventType;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameEventServiceTest {

    @Mock
    private GameEventRepository gameEventRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameEventService gameEventService;

    @Test
    @DisplayName("게임 시작")
    void gameStartedEvent() {
        // given
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("host", null, null));
        Question.create(quiz, "question");
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("user"), quiz, LobbyOptions.defaultOptions());

        Long gameId = 1L;
        Game game = Game.start(lobby);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        // when
        gameEventService.started(gameId);

        // then
        ArgumentCaptor<GameEvent> captor = ArgumentCaptor.forClass(GameEvent.class);
        verify(gameEventRepository, times(1)).save(captor.capture());

        GameEvent gameEvent = captor.getValue();
        assertThat(gameEvent).isNotNull();
        assertThat(gameEvent.getGame()).isEqualTo(game);
        assertThat(gameEvent.getType()).isEqualTo(GameEventType.GAME_STARTED);
    }


}