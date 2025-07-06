package ppalatjyo.server.domain.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.domain.game.exception.GameAlreadyEndedException;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.lobby.domain.LobbyOptions;
import ppalatjyo.server.domain.lobby.domain.LobbyStatus;
import ppalatjyo.server.domain.quiz.domain.Answer;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GameTest {

    @Test
    @DisplayName("Game 생성")
    void start() {
        // given
        User user = User.createGuest("user");

        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question question = Question.create(quiz, "question1");
        Answer.createAnswer(question, "answer1");

        Lobby lobby = Lobby.create("lobby", user, quiz, LobbyOptions.defaultOptions());

        // when
        Game game = Game.start(lobby);

        // then
        assertThat(game.getLobby()).isEqualTo(lobby);
        assertThat(game.getQuiz()).isEqualTo(quiz);
        assertThat(game.getCurrentQuestion()).isEqualTo(quiz.getQuestions().getFirst());
        assertThat(game.getStartedAt()).isNotNull();
        assertThat(game.getOptions().getMinPerGame()).isEqualTo(lobby.getOptions().getMinPerGame());
        assertThat(game.getOptions().getSecPerQuestion()).isEqualTo(lobby.getOptions().getSecPerQuestion());
        assertThat(game.isEnded()).isFalse();

        assertThat(game.getUserGames().size()).isEqualTo(1);

        assertThat(lobby.getStatus()).isEqualTo(LobbyStatus.IN_GAME);
    }

    @Test
    @DisplayName("Game 끝")
    void end() {
        // given
        User user = User.createGuest("user");

        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question.create(quiz,"question1");

        Lobby lobby = Lobby.create("lobby", user, quiz, LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        game.end();

        // then
        assertThat(game.isEnded()).isTrue();
        assertThat(game.getEndedAt()).isNotNull();
        assertThat(lobby.getStatus()).isEqualTo(LobbyStatus.WAITING);
    }

    @Test
    @DisplayName("Game 끝 - 이미 끝난 게임을 끝낼 시 예외 발생")
    void endExceptionWhenGameAlreadyEnded() {
        // given
        User user = User.createGuest("user");

        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question.create(quiz,"question1");

        Lobby lobby = Lobby.create("lobby", user, quiz, LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);
        game.end();

        // when
        assertThatThrownBy(game::end)
                .isInstanceOf(GameAlreadyEndedException.class);
    }

    @Test
    @DisplayName("다음 문제 확인 - 다음 문제가 있을 때 True 반환")
    void hasNextQuestionTrue() {
        // given
        User user = User.createGuest("user");
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question.create(quiz, "question1");
        Question.create(quiz, "question2");
        Lobby lobby = Lobby.create("lobby", user, quiz, LobbyOptions.defaultOptions());

        // when
        Game game = Game.start(lobby);

        // then
        assertThat(game.hasNextQuestion()).isTrue();
    }

    @Test
    @DisplayName("다음 문제 확인 - 다음 문제가 없을 때 False 반환")
    void hasNextQuestionFalse() {
        // given
        User user = User.createGuest("user");
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question.create(quiz, "question1");
        Lobby lobby = Lobby.create("lobby", user, quiz, LobbyOptions.defaultOptions());

        // when
        Game game = Game.start(lobby);

        // then
        assertThat(game.hasNextQuestion()).isFalse();
    }

    @Test
    @DisplayName("다음 문제 - 다음 문제가 있으면 넘어감")
    void nextQuestion() {
        // given
        User user = User.createGuest("user");

        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question.create(quiz,"question1");
        Question.create(quiz, "question2");

        Lobby lobby = Lobby.create("lobby", user, quiz, LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        game.nextQuestion();

        // then
        assertThat(game.getCurrentQuestion()).isEqualTo(quiz.getQuestions().get(1));
    }

    @Test
    @DisplayName("다음 문제 - 다음 문제가 없으면 게임 종료")
    void endGameWhenNextQuestionNotExists() {
        // given
        User user = User.createGuest("user");

        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question.create(quiz,"question1");

        Lobby lobby = Lobby.create("lobby", user, quiz, LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        game.nextQuestion();

        // then
        assertThat(game.isEnded()).isTrue();
    }
}