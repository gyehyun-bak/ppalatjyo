package ppalatjyo.server.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.game.dto.GameEventDto;
import ppalatjyo.server.game.dto.GameEventType;
import ppalatjyo.server.game.dto.NewQuestionDto;
import ppalatjyo.server.game.dto.SubmitAnswerRequestDto;
import ppalatjyo.server.game.event.LeaderboardUpdateEvent;
import ppalatjyo.server.game.event.RightAnswerEvent;
import ppalatjyo.server.game.event.TimeOutEvent;
import ppalatjyo.server.gamelog.GameLogService;
import ppalatjyo.server.global.scheduler.SchedulerService;
import ppalatjyo.server.global.websocket.aop.SendAfterCommitDto;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.message.MessageService;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.MessageRepository;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.quiz.repository.QuestionRepository;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.usergame.UserGame;
import ppalatjyo.server.usergame.UserGameRepository;
import ppalatjyo.server.userlobby.UserLobby;

import java.util.List;
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
    private GameLogService gameLogService;
    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private MessageService messageService;
    @Mock
    private SchedulerService schedulerService;

    @InjectMocks
    private GameService gameService;

    @Test
    @DisplayName("게임 시작")
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
        SendAfterCommitDto<GameEventDto> dto = gameService.start(lobbyId);

        // then
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).save(captor.capture());
        verify(gameLogService).started(any());
        verify(messageService).sendSystemMessage(any(), any());
        verify(schedulerService).runAfterSeconds(anyInt(), any(Runnable.class));
        verify(schedulerService).runAfterMinutes(anyInt(), any(Runnable.class));

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
    @DisplayName("새 문제 제시")
    void publishNewQuestion() {
        // given
        long lobbyId = 1L;
        long questionId = 1L;
        String content = "content";
        NewQuestionDto newQuestionDto = new NewQuestionDto(questionId, content);

        // when
        SendAfterCommitDto<GameEventDto> dto = gameService.publishNewQuestion(1L, lobbyId, 10, newQuestionDto);

        // then
        String destination = dto.getDestination();
        GameEventDto data = dto.getData();
        assertThat(destination).isEqualTo("/lobbies/" + lobbyId);
        assertThat(data).isNotNull();
        assertThat(data.getNewQuestion().getQuestionId()).isEqualTo(questionId);
        assertThat(data.getNewQuestion().getContent()).isEqualTo(content);
        verify(schedulerService).runAfterSeconds(anyInt(), any(Runnable.class));
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
        verify(schedulerService).runAfterSeconds(anyInt(), any(Runnable.class));
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
        Long lobbyId = 123L;

        Long gameId = 1L;
        Game game = createGame(LobbyOptions.defaultOptions());

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        // when
        SendAfterCommitDto<GameEventDto> dto = gameService.end(gameId);

        // then
        assertThat(game.isEnded()).isTrue();
        assertThat(dto.getDestination()).isEqualTo("/lobbies/null");
        assertThat(dto.getData().getType()).isEqualTo(GameEventType.GAME_ENDED);
    }

    @Test
    @DisplayName("문제 시간 초과")
    void timeOut() {
        // given
        Long gameId = 1L;
        Game game = createGame(LobbyOptions.defaultOptions());

        Question currentQuestion = game.getCurrentQuestion();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(questionRepository.findById(currentQuestion.getId())).thenReturn(Optional.of(currentQuestion));

        // when
        SendAfterCommitDto<GameEventDto> dto = gameService.timeOut(gameId, currentQuestion.getId());

        // then
        assertThat(dto.getDestination()).isEqualTo("/lobbies/null");
        assertThat(dto.getData().getType()).isEqualTo(GameEventType.TIME_OUT);
    }

    @Test
    @DisplayName("정답 제출 - 정답")
    void submitAnswer() {
        // given
        String answer = "answer";

        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        Question question1 = Question.create(quiz, "question1");
        Answer.createAnswer(question1, answer);

        Long lobbyId = 1L;
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("host"), quiz, LobbyOptions.defaultOptions());

        Long gameId = 1L;
        Game game = Game.start(lobby);

        User user = User.createGuest("user");
        Long userGameId = 1L;
        UserGame userGame = UserGame.join(user, game);

        Long messageId = 1L;
        Message message = Message.chatMessage(answer, user, lobby, game);

        SubmitAnswerRequestDto requestDto = new SubmitAnswerRequestDto();
        requestDto.setGameId(gameId);
        requestDto.setLobbyId(lobbyId);
        requestDto.setUserGameId(userGameId);
        requestDto.setMessageId(messageId);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(userGameRepository.findById(userGameId)).thenReturn(Optional.of(userGame));
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(userGameRepository.findByGameIdOrderByScoreDesc(gameId)).thenReturn(List.of(userGame));

        // when
        gameService.submitAnswer(requestDto);

        // then
        verify(gameLogService, times(1)).rightAnswer(any(), any(), any());
        verify(eventPublisher).publishEvent(any(RightAnswerEvent.class));
        verify(userGameRepository).findByGameIdOrderByScoreDesc(gameId);
        verify(eventPublisher).publishEvent(any(LeaderboardUpdateEvent.class));
        assertThat(userGame.getScore()).isEqualTo(1);
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
        Message message = Message.chatMessage(wrongAnswer, user, lobby, game); // 오답 작성

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
        verify(gameLogService).wrongAnswer(any(), any(), any());
        verify(eventPublisher, never()).publishEvent(any(RightAnswerEvent.class));
        verify(userGameRepository, never()).findByGameIdOrderByScoreDesc(any());
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
}