package ppalatjyo.server.lobby;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LobbyTest {

    @Test
    @DisplayName("Lobby 생성")
    void createLobby() {
        // given
        String name = "name";
        User host = User.createGuest("user");
        Quiz quiz = Quiz.createQuiz("quiz");
        int maxUsers = 10;
        int minPerGame = 10;
        int secPerQuestion = 60;

        LobbyOptions options = LobbyOptions.createOptions(maxUsers, minPerGame, secPerQuestion);

        // when
        Lobby lobby = Lobby.createLobby(name, host, quiz, options);

        // then
        assertThat(lobby.getName()).isEqualTo(name);
        assertThat(lobby.getHost()).isEqualTo(host);
        assertThat(lobby.getQuiz()).isEqualTo(quiz);
        assertThat(lobby.getOptions().getMaxUsers()).isEqualTo(maxUsers);
        assertThat(lobby.getOptions().getMinPerGame()).isEqualTo(minPerGame);
        assertThat(lobby.getOptions().getSecPerQuestion()).isEqualTo(secPerQuestion);
    }

    @Test
    @DisplayName("Lobby 삭제")
    void deleteLobby() {
        // given
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("host"), Quiz.createQuiz("quiz"), LobbyOptions.createOptions(1, 1, 1));

        // when
        lobby.delete();

        // then
        assertThat(lobby.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("삭제된 Lobby는 삭제할 수 없음")
    void lobbyAlreadyDeleted() {
        // given
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("host"), Quiz.createQuiz("quiz"), LobbyOptions.createOptions(1, 1, 1));
        lobby.delete();

        // when
        assertThatThrownBy(lobby::delete)
                .isInstanceOf(LobbyAlreadyDeletedException.class);
    }

    @Test
    @DisplayName("Host 변경")
    void changeHost() {
        // given
        User oldHost = User.createGuest("oldHost");
        User newHost = User.createGuest("newHost");

        Lobby lobby = Lobby.createLobby("lobby", oldHost, Quiz.createQuiz("quiz"), LobbyOptions.createOptions(1, 1, 1));

        // when
        lobby.changeHost(newHost);

        // then
        assertThat(lobby.getHost()).isEqualTo(newHost);
    }

    @Test
    @DisplayName("Options 변경")
    void changeOptions() {
        // given
        LobbyOptions options = LobbyOptions.createOptions(10, 10, 10);
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("host"), Quiz.createQuiz("quiz"), options);
        LobbyOptions newOptions = LobbyOptions.createOptions(20, 20, 20);

        // when
        lobby.changeOptions(newOptions);

        // then
        assertThat(lobby.getOptions().getMaxUsers()).isEqualTo(newOptions.getMaxUsers());
        assertThat(lobby.getOptions().getMinPerGame()).isEqualTo(newOptions.getMinPerGame());
        assertThat(lobby.getOptions().getSecPerQuestion()).isEqualTo(newOptions.getSecPerQuestion());
    }

    @Test
    @DisplayName("Quiz 변경")
    void changeQuiz() {
        // given
        Quiz oldQuiz = Quiz.createQuiz("oldQuiz");
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("host"), oldQuiz, LobbyOptions.createOptions(1, 1, 1));

        Quiz newQuiz = Quiz.createQuiz("newQuiz");

        // when
        lobby.changeQuiz(newQuiz);

        // then
        assertThat(lobby.getQuiz()).isEqualTo(newQuiz);
    }
}