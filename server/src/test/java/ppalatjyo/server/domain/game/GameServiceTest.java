package ppalatjyo.server.domain.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.domain.game.dto.GameEventDto;
import ppalatjyo.server.domain.game.dto.GameEventType;
import ppalatjyo.server.domain.game.dto.NewQuestionDto;
import ppalatjyo.server.domain.game.dto.SubmitAnswerRequestDto;
import ppalatjyo.server.global.scheduler.SchedulerService;
import ppalatjyo.server.global.websocket.aop.SendAfterCommitDto;
import ppalatjyo.server.domain.lobby.LobbyRepository;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.lobby.domain.LobbyOptions;
import ppalatjyo.server.domain.message.MessageService;
import ppalatjyo.server.domain.message.domain.Message;
import ppalatjyo.server.domain.message.MessageRepository;
import ppalatjyo.server.domain.quiz.domain.Answer;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.quiz.repository.QuestionRepository;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.usergame.UserGame;
import ppalatjyo.server.domain.usergame.UserGameRepository;
import ppalatjyo.server.domain.userlobby.UserLobby;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock private GameRepository gameRepository;
    @Mock private UserGameRepository userGameRepository;
    @Mock private LobbyRepository lobbyRepository;
    @Mock private QuestionRepository questionRepository;
    @Mock private MessageRepository messageRepository;
    @Mock private MessageService messageService;
    @Mock private SchedulerService schedulerService;

    @InjectMocks private GameService gameService;

    @Test
    @DisplayName("게임 시작")
    void start() {
        // given
        Long lobbyId = 1L;

        User host = User.createMember("host", "email", null);
        User participant = User.createGuest("user");

        Quiz quiz = Quiz.createQuiz("quiz", host);
        Question question = Question.create(quiz, "question");

        Lobby lobby = Lobby.create("lobby", host, quiz, LobbyOptions.defaultOptions());
        UserLobby.join(participant, lobby);

        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        // when
        SendAfterCommitDto<GameEventDto> dto = gameService.start(lobbyId);

        // then
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).save(captor.capture());
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
        assertThat(destination).isEqualTo("/lobbies/" + lobbyId + "/games/events");
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
        assertThat(dto.getDestination()).isEqualTo("/lobbies/null/games/events");
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
        assertThat(dto.getDestination()).isEqualTo("/lobbies/null/games/events");
        assertThat(dto.getData().getType()).isEqualTo(GameEventType.TIME_OUT);
    }

    @Test
    @DisplayName("정답 제출 - 정답")
    void submitAnswer() {
        // given
        String answer = "answer";

        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question question1 = Question.create(quiz, "question1");
        Answer.createAnswer(question1, answer);

        Long lobbyId = 1L;
        Lobby lobby = Lobby.create("lobby", User.createGuest("host"), quiz, LobbyOptions.defaultOptions());

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
        SendAfterCommitDto<GameEventDto> dto = gameService.submitAnswer(requestDto);

        // then
        verify(userGameRepository).findByGameIdOrderByScoreDesc(gameId);
        verify(messageService).sendSystemMessage(any(), any());
        assertThat(userGame.getScore()).isEqualTo(1);
        assertThat(dto.getDestination()).isEqualTo("/lobbies/null/games/events");
        assertThat(dto.getData().getType()).isEqualTo(GameEventType.CORRECT);
        assertThat(dto.getData().getAnswerInfo().getCorrectUserNickname()).isEqualTo("user");
    }

    @Test
    @DisplayName("정답 제출 - 오답")
    void submitWrongAnswer() {
        // given
        String answer = "answer1";
        String wrongAnswer = "wrongAnswer";

        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question question1 = Question.create(quiz, "question1");
        Answer.createAnswer(question1, answer);

        Lobby lobby = Lobby.create("lobby", User.createGuest("host"), quiz, LobbyOptions.defaultOptions());

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
        SendAfterCommitDto<GameEventDto> dto = gameService.submitAnswer(requestDto);

        // then
        assertThat(dto).isNull();
        verify(userGameRepository, never()).findByGameIdOrderByScoreDesc(any());
    }
    
    @Test
    @DisplayName("lobbyId로 게임 전부 종료")
    void endGamesByLobbyId() {
        // given
        Long lobbyId = 1L;
        Game game = mock(Game.class);
        when(gameRepository.findByLobbyId(lobbyId)).thenReturn(List.of(game));

        // when
        gameService.endGamesByLobbyId(lobbyId);
        
        // then
        verify(game).end();
    }

    private Game createGame(LobbyOptions options) {
        Lobby lobby = Lobby.create("lobby", User.createGuest("host"), createQuiz(), options);
        return Game.start(lobby);
    }

    private Quiz createQuiz() {
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question question1 = Question.create(quiz, "question1");
        Answer.createAnswer(question1, "answer1");
        Question question2 = Question.create(quiz, "question2");
        Answer.createAnswer(question2, "answer2");
        return quiz;
    }
}