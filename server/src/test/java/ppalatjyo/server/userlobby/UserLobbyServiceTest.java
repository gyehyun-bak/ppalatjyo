package ppalatjyo.server.userlobby;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserLobbyServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserLobbyRepository userLobbyRepository;

    @InjectMocks
    private UserLobbyService userLobbyService;

    @Test
    @DisplayName("User가 Lobby에 참가")
    void join() {
        // given
        Long userId = 1L;
        Long lobbyId = 1L;

        User host = User.createMember("host", "email", "provider");
        Lobby lobby = Lobby.createLobby("lobby", host, Quiz.createQuiz("quiz", host), LobbyOptions.defaultOptions());
        User user = User.createGuest("user");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        // when
        userLobbyService.join(userId, lobbyId);

        // then
        ArgumentCaptor<UserLobby> captor = ArgumentCaptor.forClass(UserLobby.class);
        verify(userLobbyRepository, times(1)).save(captor.capture());

        UserLobby userLobby = captor.getValue();

        assertThat(userLobby.getUser()).isEqualTo(user);
        assertThat(userLobby.getLobby()).isEqualTo(lobby);
        assertThat(userLobby.getJoinedAt()).isNotNull();
    }

    @Test
    @DisplayName("User가 Lobby에서 나감")
    void leave() {
        // given
        Long userId = 1L;
        Long lobbyId = 1L;

        User host = User.createMember("host", "email", "provider");
        Lobby lobby = Lobby.createLobby("lobby", host, Quiz.createQuiz("quiz", host), LobbyOptions.defaultOptions());
        User user = User.createGuest("user");

        UserLobby userLobby = UserLobby.join(user, lobby);

        when(userLobbyRepository.findByUserIdAndLobbyId(userId, lobbyId)).thenReturn(Optional.of(userLobby));

        // when
        userLobbyService.leave(userId, lobbyId);

        // then
        assertThat(userLobby.isLeft()).isTrue();
    }
}