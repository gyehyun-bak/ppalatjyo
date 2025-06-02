package ppalatjyo.server.lobby;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.quiz.Quiz;
import ppalatjyo.server.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class LobbyTest {

    @Test
    @DisplayName("Lobby 기본 설정으로 생성")
    void createLobby() {
        // given
        User host = User.createGuest("user");
        Quiz quiz = Quiz.createQuiz("quiz");

        // when
        Lobby lobby = Lobby.createLobby(host, quiz);

        // then
        assertThat(lobby.getHost()).isEqualTo(host);
        assertThat(lobby.getQuiz()).isEqualTo(quiz);

        assertThat(lobby.getCreatedAt()).isNotNull();
        assertThat(lobby.getLastModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("Lobby 옵션과 함께 생성")
    void createLobbyWithOptions() {
        // given
        User host = User.createGuest("user");
        Quiz quiz = Quiz.createQuiz("quiz");
        int maxUsers = 10;
        int timePerGame = 300;
        int timePerQuestion = 10;

        // when
        Lobby lobby = Lobby.createLobbyWithOptions(host, quiz, maxUsers, timePerGame, timePerQuestion);

        // then
        assertThat(lobby.getHost()).isEqualTo(host);
        assertThat(lobby.getQuiz()).isEqualTo(quiz);
        assertThat(lobby.getMaxUsers()).isEqualTo(maxUsers);
        assertThat(lobby.getTimePerGame()).isEqualTo(timePerGame);
        assertThat(lobby.getTimePerQuestion()).isEqualTo(timePerQuestion);

        assertThat(lobby.getCreatedAt()).isNotNull();
        assertThat(lobby.getLastModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("Lobby 삭제")
    void deleteLobby() {
        // given
        Lobby lobby = Lobby.createLobby(User.createGuest("host"), Quiz.createQuiz("quiz"));

        // when
        lobby.delete();

        // then
        assertThat(lobby.getDeletedAt()).isNotNull();
    }
}