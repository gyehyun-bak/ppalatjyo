package ppalatjyo.server.gameevent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.game.Game;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.message.Message;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.usergame.UserGame;

import static org.assertj.core.api.Assertions.assertThat;

class GameEventTest {

    @Test
    @DisplayName("게임 시작됨")
    void gameStartedEvent () {
        // given
        User user = User.createGuest("guest");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);
        UserGame userGame = UserGame.join(user, game);

        // when
        GameEvent gameEvent = GameEvent.started(game, userGame);

        // then
        assertThat(gameEvent.getGame()).isEqualTo(game);
        assertThat(gameEvent.getUserGame()).isEqualTo(userGame);
        assertThat(gameEvent.getType()).isEqualTo(GameEventType.GAME_STARTED);
    }

    @Test
    @DisplayName("게임 종료됨")
    void gameEndedEvent () {
        // given
        User user = User.createGuest("guest");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        GameEvent gameEvent = GameEvent.ended(game);

        // then
        assertThat(gameEvent.getGame()).isEqualTo(game);
        assertThat(gameEvent.getType()).isEqualTo(GameEventType.GAME_ENDED);
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
        Message message = Message.create(wrongAnswer, user, lobby, game);

        // when
        GameEvent gameEvent = GameEvent.wrongAnswer(game, userGame, message);

        // then
        assertThat(gameEvent.getType()).isEqualTo(GameEventType.WRONG_ANSWER);
        assertThat(gameEvent.getGame()).isEqualTo(game);
        assertThat(gameEvent.getUserGame()).isEqualTo(userGame);
        assertThat(gameEvent.getMessage()).isEqualTo(message);
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
        Message message = Message.create(rightAnswer, user, lobby, game);

        // when
        GameEvent gameEvent = GameEvent.rightAnswer(game, userGame, message);

        // then
        assertThat(gameEvent.getType()).isEqualTo(GameEventType.RIGHT_ANSWER);
        assertThat(gameEvent.getGame()).isEqualTo(game);
        assertThat(gameEvent.getUserGame()).isEqualTo(userGame);
        assertThat(gameEvent.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("시간 초과")
    void timeOut() {
        // given
        User user = User.createGuest("guest");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        GameEvent gameEvent = GameEvent.timeOut(game);

        // then
        assertThat(gameEvent.getType()).isEqualTo(GameEventType.TIME_OUT);
        assertThat(gameEvent.getGame()).isEqualTo(game);
    }

    private Quiz getQuiz() {
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        Question.create(quiz,"question1", Answer.createAnswer("answer1"));
        Question.create(quiz, "question2", Answer.createAnswer("answer2"));
        return quiz;
    }
}