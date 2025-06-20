package ppalatjyo.server.gameevent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.game.GameRepository;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.gameevent.domain.GameEvent;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        gameEventService.start(gameId);

        // then
        verify(gameEventRepository, times(1)).save(any(GameEvent.class));
    }


}