package ppalatjyo.server.domain.userlobby;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.domain.message.MessageService;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.userlobby.UserLobbyEventHandler;
import ppalatjyo.server.domain.userlobby.event.UserJoinedLobbyEvent;
import ppalatjyo.server.domain.userlobby.event.UserLeftLobbyEvent;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLobbyEventHandlerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserLobbyEventHandler userLobbyEventHandler;

    @Test
    void handleUserJoinedLobbyEvent() {
        // given
        Long userId = 1L;
        Long lobbyId = 2L;
        UserJoinedLobbyEvent event = new UserJoinedLobbyEvent(userId, lobbyId);

        User user = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getNickname()).thenReturn("nickname");

        // when
        userLobbyEventHandler.handleUserJoinedLobbyEvent(event);

        // then
        verify(messageService).sendSystemMessage(anyString(), anyLong());
    }

    @Test
    void handleUserLeftLobbyEvent() {
        // given
        Long userId = 1L;
        Long lobbyId = 2L;
        UserLeftLobbyEvent event = new UserLeftLobbyEvent(userId, lobbyId);

        User user = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getNickname()).thenReturn("nickname");

        // when
        userLobbyEventHandler.handleUserLeftLobbyEvent(event);

        // then
        verify(messageService).sendSystemMessage(anyString(), anyLong());
    }
}