package ppalatjyo.server.gamelog;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.gamelog.domain.GameLog;
import ppalatjyo.server.gamelog.domain.GameLogType;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.usergame.UserGame;

import static org.assertj.core.api.Assertions.assertThat;

class GameLogTest {

    @Test
    @DisplayName("게임 시작됨")
    void gameStartedEvent () {
        // given
        User user = User.createGuest("guest");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        GameLog gameLog = GameLog.started(game);

        // then
        assertThat(gameLog.getGame()).isEqualTo(game);
        assertThat(gameLog.getType()).isEqualTo(GameLogType.GAME_STARTED);
    }

    @Test
    @DisplayName("게임 종료됨")
    void gameEndedEvent () {
        // given
        User user = User.createGuest("guest");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        GameLog gameLog = GameLog.ended(game);

        // then
        assertThat(gameLog.getGame()).isEqualTo(game);
        assertThat(gameLog.getType()).isEqualTo(GameLogType.GAME_ENDED);
    }

    @Test
    @DisplayName("오답")
    void wrongAnswer() {
        // given
        String wrongAnswer = "Wrong Answer";
        User user = User.createGuest("guest");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);
        UserGame userGame = UserGame.join(user, game);
        Message message = Message.chatMessage(wrongAnswer, user, lobby, game);

        // when
        GameLog gameLog = GameLog.wrongAnswer(game, userGame, message);

        // then
        assertThat(gameLog.getType()).isEqualTo(GameLogType.WRONG_ANSWER);
        assertThat(gameLog.getGame()).isEqualTo(game);
        assertThat(gameLog.getUserGame()).isEqualTo(userGame);
        assertThat(gameLog.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("정답")
    void rightAnswer() {
        // given
        String rightAnswer = "Right Answer";
        User user = User.createGuest("guest");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);
        UserGame userGame = UserGame.join(user, game);
        Message message = Message.chatMessage(rightAnswer, user, lobby, game);

        // when
        GameLog gameLog = GameLog.rightAnswer(game, userGame, message);

        // then
        assertThat(gameLog.getType()).isEqualTo(GameLogType.RIGHT_ANSWER);
        assertThat(gameLog.getGame()).isEqualTo(game);
        assertThat(gameLog.getUserGame()).isEqualTo(userGame);
        assertThat(gameLog.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("시간 초과")
    void timeOut() {
        // given
        User user = User.createGuest("guest");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        GameLog gameLog = GameLog.timeOut(game);

        // then
        assertThat(gameLog.getType()).isEqualTo(GameLogType.TIME_OUT);
        assertThat(gameLog.getGame()).isEqualTo(game);
    }

    private Quiz getQuiz() {
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        Question question1 = Question.create(quiz, "question1");
        Answer.createAnswer(question1, "answer1");
        Question question2 = Question.create(quiz, "question2");
        Answer.createAnswer(question2, "answer2");
        return quiz;
    }
}