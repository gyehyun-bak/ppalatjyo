package ppalatjyo.server.userlobby;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.user.domain.User;

import static org.junit.jupiter.api.Assertions.*;

class UserLobbyTest {

    @Test
    @DisplayName("UserLobby 생성")
    void createUserLobby() {
        // given
        User user = User.createGuest("user");
        Lobby.createLobby("lobby", user, null, null);

        // when

        // then
    }
}