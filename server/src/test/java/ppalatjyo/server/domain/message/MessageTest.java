package ppalatjyo.server.domain.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.lobby.domain.LobbyOptions;
import ppalatjyo.server.domain.message.domain.Message;
import ppalatjyo.server.domain.message.domain.MessageType;
import ppalatjyo.server.domain.quiz.domain.Answer;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    @Test
    @DisplayName("채팅 메시지: 유저 -> 로비")
    void chatMessage() {
        // given
        String content = "content";
        User user = User.createGuest("user");
        Lobby lobby = Lobby.create("lobby", user, null, LobbyOptions.defaultOptions());

        // when
        Message message = Message.chatMessage(content, user, lobby);

        // then
        assertThat(message.getContent()).isEqualTo(content);
        assertThat(message.getUser()).isEqualTo(user);
        assertThat(message.getLobby()).isEqualTo(lobby);
    }

    @Test
    @DisplayName("채팅 메시지: 유저 -> 게임")
    void chatMessageToGame() {
        // given
        String content = "content";
        User user = User.createGuest("user");
        Lobby lobby = Lobby.create("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
        Game game = Game.start(lobby);

        // when
        Message message = Message.chatMessage(content, user, lobby, game);

        // then
        assertThat(message.getContent()).isEqualTo(content);
        assertThat(message.getUser()).isEqualTo(user);
        assertThat(message.getLobby()).isEqualTo(lobby);
        assertThat(message.getGame()).isEqualTo(game);
    }

    @Test
    @DisplayName("시스템 메시지")
    void chatMessageSystemMessage() {
        // given
        String content = "content";
        User user = User.createGuest("user");
        Lobby lobby = Lobby.create("lobby", user, getQuiz(), LobbyOptions.defaultOptions());

        // when
        Message message = Message.systemMessage(content, lobby);

        // then
        assertThat(message.getType()).isEqualTo(MessageType.SYSTEM);
        assertThat(message.getContent()).isEqualTo(content);
        assertThat(message.getUser()).isNull();
        assertThat(message.getLobby()).isEqualTo(lobby);
        assertThat(message.getGame()).isNull();
    }

    private Quiz getQuiz() {
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", null));
        Question question1 = Question.create(quiz, "question1");
        Answer.createAnswer(question1, "answer1");
        Question question2 = Question.create(quiz, "question2");
        Answer.createAnswer(question2, "answer2");
        return quiz;
    }
}