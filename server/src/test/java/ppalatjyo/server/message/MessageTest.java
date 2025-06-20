package ppalatjyo.server.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.domain.MessageType;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    @Test
    @DisplayName("채팅 메시지: 유저 -> 로비")
    void chatMessage() {
        // given
        String content = "content";
        User user = User.createGuest("user");
        Lobby lobby = Lobby.createLobby("lobby", user, null, LobbyOptions.defaultOptions());

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
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());
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
        Lobby lobby = Lobby.createLobby("lobby", user, getQuiz(), LobbyOptions.defaultOptions());

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
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        Question question1 = Question.create(quiz, "question1");
        Answer.createAnswer(question1, "answer1");
        Question question2 = Question.create(quiz, "question2");
        Answer.createAnswer(question2, "answer2");
        return quiz;
    }
}