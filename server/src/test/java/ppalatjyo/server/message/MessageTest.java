package ppalatjyo.server.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.game.Game;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    @Test
    @DisplayName("메시지 생성: 유저 -> 로비")
    void create() {
        // given
        String content = "content";
        User user = User.createGuest("user");
        Lobby lobby = Lobby.createLobby("lobby", user, null, LobbyOptions.defaultOptions());

        // when
        Message message = Message.create(content, user, lobby);

        // then
        assertThat(message.getContent()).isEqualTo(content);
        assertThat(message.getUser()).isEqualTo(user);
        assertThat(message.getLobby()).isEqualTo(lobby);
    }

    @Test
    @DisplayName("메시지 생성: 유저 -> 게임")
    void createToGame() {
        // given
        String content = "content";
        User user = User.createGuest("user");
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        Message message = Message.create(content, user, lobby, game);

        // then
        assertThat(message.getContent()).isEqualTo(content);
        assertThat(message.getUser()).isEqualTo(user);
        assertThat(message.getLobby()).isEqualTo(lobby);
        assertThat(message.getGame()).isEqualTo(game);
    }

    private Quiz getQuiz() {
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        Question.create(quiz,"question1", Answer.createAnswer("answer1"));
        Question.create(quiz, "question2", Answer.createAnswer("answer2"));
        return quiz;
    }
}