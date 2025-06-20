package ppalatjyo.server.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.game.dto.SubmitAnswerRequestDto;
import ppalatjyo.server.gameevent.GameEventService;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.message.Message;
import ppalatjyo.server.message.MessageRepository;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.usergame.UserGame;
import ppalatjyo.server.usergame.UserGameRepository;
import ppalatjyo.server.userlobby.UserLobby;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserGameRepository userGameRepository;

    @Mock
    private GameEventService gameEventService;

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private MessageRepository messageRepository;

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
        Question question = Question.create(quiz, "question");

        Lobby lobby = Lobby.createLobby("lobby", host, quiz, LobbyOptions.defaultOptions());
        UserLobby.join(participant, lobby);

        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        // when
        gameService.start(lobbyId);

        // then
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository, times(1)).save(captor.capture());
        verify(gameEventService, times(1)).started(any());

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
    void endGame() {
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
        Question question1 = Question.create(quiz, "question1");
        Answer.createAnswer(question1, "answer1");
        Question question2 = Question.create(quiz, "question2");
        Answer.createAnswer(question2, "answer2");
        return quiz;
    }

    @Test
    @DisplayName("정답 제출 - 정답")
    void submitAnswer() {
        // given
        String answer = "answer";

        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        Question question1 = Question.create(quiz, "question1");
        Answer.createAnswer(question1, answer);

        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("host"), quiz, LobbyOptions.defaultOptions());

        Long gameId = 1L;
        Game game = Game.start(lobby);

        User user = User.createGuest("user");
        Long userGameId = 1L;
        UserGame userGame = UserGame.join(user, game);

        Long messageId = 1L;
        Message message = Message.create(answer, user, lobby, game);

        SubmitAnswerRequestDto requestDto = new SubmitAnswerRequestDto();
        requestDto.setGameId(gameId);
        requestDto.setUserGameId(userGameId);
        requestDto.setMessageId(messageId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userGameRepository.findById(userGameId)).thenReturn(Optional.of(userGame));
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // when
        gameService.submitAnswer(requestDto);

        // then
        verify(gameEventService, times(1)).rightAnswer(any(), any(), any());
    }

    @Test
    @DisplayName("정답 제출 - 오답")
    void submitWrongAnswer() {
        // given
        String answer = "answer1";
        String wrongAnswer = "wrongAnswer";

        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        Question question1 = Question.create(quiz, "question1");
        Answer.createAnswer(question1, answer);

        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("host"), quiz, LobbyOptions.defaultOptions());

        Long gameId = 1L;
        Game game = Game.start(lobby);

        User user = User.createGuest("user");
        Long userGameId = 1L;
        UserGame userGame = UserGame.join(user, game);

        Long messageId = 1L;
        Message message = Message.create(wrongAnswer, user, lobby, game); // 오답 작성

        SubmitAnswerRequestDto requestDto = new SubmitAnswerRequestDto();
        requestDto.setGameId(gameId);
        requestDto.setUserGameId(userGameId);
        requestDto.setMessageId(messageId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userGameRepository.findById(userGameId)).thenReturn(Optional.of(userGame));
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // when
        gameService.submitAnswer(requestDto);

        // then
        verify(gameEventService, times(1)).wrongAnswer(any(), any(), any());
    }
}