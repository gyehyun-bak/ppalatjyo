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
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.MessageRepository;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.usergame.UserGame;
import ppalatjyo.server.usergame.UserGameRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameEventServiceTest {

    @Mock
    private GameEventRepository gameEventRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserGameRepository userGameRepository;

    @Mock
    private MessageRepository messageRepository;

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

    @Test
    @DisplayName("게임 종료")
    void ended() {
        // given
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("host", null, null));
        Question.create(quiz, "question");
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("user"), quiz, LobbyOptions.defaultOptions());

        Long gameId = 1L;
        Game game = Game.start(lobby);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        // when
        gameEventService.ended(gameId);

        // then
        ArgumentCaptor<GameEvent> captor = ArgumentCaptor.forClass(GameEvent.class);
        verify(gameEventRepository, times(1)).save(captor.capture());
        GameEvent gameEvent = captor.getValue();
        assertThat(gameEvent).isNotNull();
        assertThat(gameEvent.getGame()).isEqualTo(game);
        assertThat(gameEvent.getType()).isEqualTo(GameEventType.GAME_ENDED);
    }

    @Test
    @DisplayName("타임 아웃")
    void timeOut() {
        // given
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("host", null, null));
        Question.create(quiz, "question");
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("user"), quiz, LobbyOptions.defaultOptions());

        Long gameId = 1L;
        Game game = Game.start(lobby);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        // when
        gameEventService.timeOut(gameId);

        // then
        ArgumentCaptor<GameEvent> captor = ArgumentCaptor.forClass(GameEvent.class);
        verify(gameEventRepository, times(1)).save(captor.capture());
        GameEvent gameEvent = captor.getValue();
        assertThat(gameEvent).isNotNull();
        assertThat(gameEvent.getGame()).isEqualTo(game);
        assertThat(gameEvent.getType()).isEqualTo(GameEventType.TIME_OUT);
    }

    @Test
    @DisplayName("정답")
    void rightAnswer() {
        // given
        User user = User.createMember("host", null, null);
        Quiz quiz = Quiz.createQuiz("quiz", user);
        Question.create(quiz, "question");
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("user"), quiz, LobbyOptions.defaultOptions());

        Long gameId = 1L;
        Game game = Game.start(lobby);

        Long userGameId = 1L;
        UserGame userGame = game.getUserGames().getFirst();

        Long messageId = 1L;
        Message message = Message.chatMessage("message", user, lobby);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userGameRepository.findById(userGameId)).thenReturn(Optional.of(userGame));
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // when
        gameEventService.rightAnswer(gameId, userGameId, messageId);

        // then
        ArgumentCaptor<GameEvent> captor = ArgumentCaptor.forClass(GameEvent.class);
        verify(gameEventRepository, times(1)).save(captor.capture());
        GameEvent gameEvent = captor.getValue();
        assertThat(gameEvent).isNotNull();
        assertThat(gameEvent.getGame()).isEqualTo(game);
        assertThat(gameEvent.getType()).isEqualTo(GameEventType.RIGHT_ANSWER);
        assertThat(gameEvent.getMessage()).isEqualTo(message);
        assertThat(gameEvent.getUserGame()).isEqualTo(userGame);
    }

    @Test
    @DisplayName("오답")
    void wrongAnswer() {
        // given
        User user = User.createMember("host", null, null);
        Quiz quiz = Quiz.createQuiz("quiz", user);
        Question.create(quiz, "question");
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("user"), quiz, LobbyOptions.defaultOptions());

        Long gameId = 1L;
        Game game = Game.start(lobby);

        Long userGameId = 1L;
        UserGame userGame = game.getUserGames().getFirst();

        Long messageId = 1L;
        Message message = Message.chatMessage("message", user, lobby);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userGameRepository.findById(userGameId)).thenReturn(Optional.of(userGame));
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // when
        gameEventService.wrongAnswer(gameId, userGameId, messageId);

        // then
        ArgumentCaptor<GameEvent> captor = ArgumentCaptor.forClass(GameEvent.class);
        verify(gameEventRepository, times(1)).save(captor.capture());
        GameEvent gameEvent = captor.getValue();
        assertThat(gameEvent).isNotNull();
        assertThat(gameEvent.getGame()).isEqualTo(game);
        assertThat(gameEvent.getType()).isEqualTo(GameEventType.WRONG_ANSWER);
        assertThat(gameEvent.getMessage()).isEqualTo(message);
        assertThat(gameEvent.getUserGame()).isEqualTo(userGame);
    }


}