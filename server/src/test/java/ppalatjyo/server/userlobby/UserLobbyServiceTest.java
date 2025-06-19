package ppalatjyo.server.userlobby;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.user.domain.User;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserLobbyServiceTest {

    @InjectMocks
    private UserLobbyService userLobbyService;

    @Test
    @DisplayName("User가 Lobby에 참가")
    void join() {
        // given
        User user = User.createGuest("user");
//        Lobby lobby = Lobby.createLobby("lobby", );

        // when

        // then
    }
}