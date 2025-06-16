package ppalatjyo.server.game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    @Test
    @DisplayName("Game 생성")
    void start() {
        // given
        User user = User.createGuest("user");

        Quiz quiz = Quiz.createQuiz("quiz");
        Question.create(quiz,"question1", Answer.createAnswer("answer1"));

        Lobby lobby = Lobby.createLobby("lobby", user, quiz, LobbyOptions.createDefaultOptions());

        // when
        Game game = Game.start(lobby);

        // then
        assertThat(game.getLobby()).isEqualTo(lobby);
        assertThat(game.getQuiz()).isEqualTo(quiz);
        assertThat(game.getCurrentQuestion()).isEqualTo(quiz.getQuestions().getFirst());
        assertThat(game.getStartedAt()).isNotNull();
        assertThat(game.getEndedAt()).isNull();
    }

    @Test
    @DisplayName("Game 끝")
    void end() {
        // given
        User user = User.createGuest("user");

        Quiz quiz = Quiz.createQuiz("quiz");
        Question.create(quiz,"question1", Answer.createAnswer("answer1"));

        Lobby lobby = Lobby.createLobby("lobby", user, quiz, LobbyOptions.createDefaultOptions());
        Game game = Game.start(lobby);

        // when
        game.end();

        // then
        assertThat(game.isEnded()).isTrue();
        assertThat(game.getEndedAt()).isNotNull();
    }

    @Test
    @DisplayName("다음 문제 확인 - 다음 문제가 있을 때 True 반환")
    void hasNextQuestionTrue() {
        // given
        User user = User.createGuest("user");
        Quiz quiz = Quiz.createQuiz("quiz");
        Question.create(quiz, "question1", Answer.createAnswer("answer1"));
        Question.create(quiz, "question2", Answer.createAnswer("answer2"));
        Lobby lobby = Lobby.createLobby("lobby", user, quiz, LobbyOptions.createDefaultOptions());

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
        Quiz quiz = Quiz.createQuiz("quiz");
        Question.create(quiz, "question1", Answer.createAnswer("answer1"));
        Lobby lobby = Lobby.createLobby("lobby", user, quiz, LobbyOptions.createDefaultOptions());

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

        Quiz quiz = Quiz.createQuiz("quiz");
        Question.create(quiz,"question1", Answer.createAnswer("answer1"));
        Question.create(quiz, "question2", Answer.createAnswer("answer2"));

        Lobby lobby = Lobby.createLobby("lobby", user, quiz, LobbyOptions.createDefaultOptions());
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

        Quiz quiz = Quiz.createQuiz("quiz");
        Question.create(quiz,"question1", Answer.createAnswer("answer1"));

        Lobby lobby = Lobby.createLobby("lobby", user, quiz, LobbyOptions.createDefaultOptions());
        Game game = Game.start(lobby);

        // when
        game.nextQuestion();

        // then
        assertThat(game.isEnded()).isTrue();
    }
}