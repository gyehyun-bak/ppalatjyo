package ppalatjyo.server.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.global.websocket.MessageBrokerService;
import ppalatjyo.server.global.websocket.dto.PublicationDto;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.event.ChatMessageSentEvent;
import ppalatjyo.server.message.event.SystemMessageSentEvent;
import ppalatjyo.server.user.domain.User;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageEventHandlerTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageBrokerService messageBrokerService;

    @InjectMocks
    private MessageEventHandler messageEventHandler;

    @Test
    @DisplayName("ChatMessageSentEvent -> '/lobbies/{lobbyId}/messages/new' 로 발행")
    void handleChatMessageSentEvent() {
        // given
        long messageId = 1L;

        ChatMessageSentEvent event = new ChatMessageSentEvent(messageId);

        Message message = mock(Message.class);
        Lobby lobby = mock(Lobby.class);
        User user = mock(User.class);

        when(messageRepository.findById(event.getMessageId())).thenReturn(Optional.of(message));
        when(message.getLobby()).thenReturn(lobby);
        when(lobby.getId()).thenReturn(1L);
        when(message.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(1L);

        // when
        messageEventHandler.handleChatMessageSentEvent(event);

        // then
        verify(messageBrokerService).publish(anyString(), any(PublicationDto.class));
    }

    @Test
    @DisplayName("SystemMessageSentEvent -> '/lobbies/{lobbyId}/messages/new' 로 발행")
    void handleSystemMessageSentEvent() {
        // given
        long messageId = 1L;

        SystemMessageSentEvent event = new SystemMessageSentEvent(messageId);

        Message message = mock(Message.class);
        Lobby lobby = mock(Lobby.class);

        when(messageRepository.findById(event.getMessageId())).thenReturn(Optional.of(message));
        when(message.getLobby()).thenReturn(lobby);
        when(lobby.getId()).thenReturn(1L);

        // when
        messageEventHandler.handleSystemMessageSentEvent(event);

        // then
        verify(messageBrokerService).publish(anyString(), any(PublicationDto.class));
    }
}