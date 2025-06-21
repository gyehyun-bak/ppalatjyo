package ppalatjyo.server.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.domain.MessageType;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MessageService messageService;

    @Test
    @DisplayName("채팅 메시지 생성/전송")
    void createChatMessage() {
        // given
        String content = "content";

        User user = User.createGuest("user");
        Lobby lobby = Lobby.createLobby("lobby", user, null, null);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(lobby));

        // when
        messageService.sendChatMessage(content, 1L, 1L);

        // then
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository, times(1)).save(captor.capture());
        verify(eventPublisher, times(1)).publishEvent(any(MessageSentEvent.class));

        Message message = captor.getValue();
        assertThat(message.getContent()).isEqualTo(content);
        assertThat(message.getUser()).isEqualTo(user);
        assertThat(message.getLobby()).isEqualTo(lobby);
        assertThat(message.getType()).isEqualTo(MessageType.CHAT);
    }
}