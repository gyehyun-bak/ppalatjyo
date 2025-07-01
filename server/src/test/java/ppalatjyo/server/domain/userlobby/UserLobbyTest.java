package ppalatjyo.server.domain.userlobby;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.lobby.domain.LobbyOptions;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.userlobby.UserLobby;

import static org.assertj.core.api.Assertions.assertThat;

class UserLobbyTest {

    @Test
    @DisplayName("UserLobby 참가")
    void join() {
        // given
        User user = User.createGuest("user");
        Lobby lobby = Lobby.createLobby("lobby", user, null, LobbyOptions.defaultOptions());

        // when
        UserLobby userLobby = UserLobby.join(user, lobby);

        // then
        assertThat(userLobby.getUser()).isEqualTo(user);
        assertThat(userLobby.getLobby()).isEqualTo(lobby);
        assertThat(userLobby.getJoinedAt()).isNotNull();
        assertThat(userLobby.getLeftAt()).isNull();
    }

    @Test
    @DisplayName("UserLobby 나가기")
    void leave() {
        // given
        User user = User.createGuest("user");
        Lobby lobby = Lobby.createLobby("lobby", user, null, LobbyOptions.defaultOptions());
        UserLobby userLobby = UserLobby.join(user, lobby);

        // when
        userLobby.leave();

        // then
        assertThat(userLobby.isLeft()).isTrue();
        assertThat(userLobby.getLeftAt()).isNotNull();
    }
}