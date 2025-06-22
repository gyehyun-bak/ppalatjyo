package ppalatjyo.server.usergame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserGameTest {

    @Test
    @DisplayName("UserGame 생성")
    void join() {
        // given
        User user = User.createGuest("guest");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        UserGame userGame = UserGame.join(user, game);

        // then
        assertThat(userGame.getUser()).isEqualTo(user);
        assertThat(userGame.getGame()).isEqualTo(game);
        assertThat(userGame.getScore()).isEqualTo(0);
    }

    @Test
    @DisplayName("득점")
    void increaseScoreBy() {
        // given
        User user = User.createGuest("guest");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);
        UserGame userGame = UserGame.join(user, game);

        // when
        userGame.increaseScoreBy(1);

        // then
        assertThat(userGame.getScore()).isEqualTo(1);
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