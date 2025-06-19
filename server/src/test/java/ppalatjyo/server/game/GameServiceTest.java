package ppalatjyo.server.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.userlobby.UserLobby;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    @DisplayName("Game 시작")
    void start() {
        // given
        Long lobbyId = 1L;

        User host = User.createMember("host", "email", "provider");
        User participant = User.createGuest("user");

        Quiz quiz = Quiz.createQuiz("quiz", host);
        Question question = Question.create(quiz, "question", Answer.createAnswer("answer"));

        Lobby lobby = Lobby.createLobby("lobby", host, quiz, LobbyOptions.defaultOptions());
        UserLobby.join(participant, lobby);

        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        // when
        gameService.start(lobbyId);

        // then
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository, times(1)).save(captor.capture());

        Game game = captor.getValue();
        assertThat(game).isNotNull();
        assertThat(game.getLobby()).isEqualTo(lobby);
        assertThat(game.getStartedAt()).isNotNull();
        assertThat(game.getQuiz()).isEqualTo(quiz);
        assertThat(game.getUserGames().size()).isEqualTo(2);
        assertThat(game.getCurrentQuestionIndex()).isEqualTo(0);
        assertThat(game.getCurrentQuestion()).isEqualTo(question);
    }

    @Test
    @DisplayName("다음 문제")
    void answer() {
        // given
        Long gameId = 1L;
        Game game = createGame(LobbyOptions.defaultOptions());

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        // when
        gameService.nextQuestion(gameId);

        // then
        assertThat(game.getCurrentQuestionIndex()).isEqualTo(1);
    }

    @Test
    @DisplayName("마지막 문제에서 다음 문제 시 게임 종료")
    void GameEndOnNextQuestionWhenLastQuestion() {
        // given
        Long gameId = 1L;
        Game game = createGame(LobbyOptions.defaultOptions()); // 문제는 총 두 개 있음

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        // when
        gameService.nextQuestion(gameId);
        gameService.nextQuestion(gameId);

        // then
        assertThat(game.isEnded()).isTrue();
    }

    @Test
    @DisplayName("게임 종료")
    void EndGame() {
        // given
        Long gameId = 1L;
        Game game = createGame(LobbyOptions.defaultOptions());

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        // when
        gameService.end(gameId);

        // then
        assertThat(game.isEnded()).isTrue();
    }

    private Game createGame(LobbyOptions options) {
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("host"), createQuiz(), options);
        return Game.start(lobby);
    }

    private Quiz createQuiz() {
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        Question.create(quiz,"question1", Answer.createAnswer("answer1"));
        Question.create(quiz, "question2", Answer.createAnswer("answer2"));
        return quiz;
    }
}