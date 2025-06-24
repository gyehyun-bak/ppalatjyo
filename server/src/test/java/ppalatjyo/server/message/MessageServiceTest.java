package ppalatjyo.server.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.global.websocket.aop.SendAfterCommitDto;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.domain.MessageType;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    @DisplayName("채팅 메시지 생성/전송")
    void sendChatMessage() {
        // given
        String content = "content";

        User user = User.createGuest("user");
        Lobby lobby = Lobby.createLobby("lobby", user, null, null);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(lobby));

        // when
        SendAfterCommitDto<MessageResponseDto> dto = messageService.sendChatMessage(content, 1L, 1L);

        // then
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository, times(1)).save(captor.capture());

        Message message = captor.getValue();
        assertThat(message.getContent()).isEqualTo(content);
        assertThat(message.getUser()).isEqualTo(user);
        assertThat(message.getLobby()).isEqualTo(lobby);
        assertThat(message.getType()).isEqualTo(MessageType.CHAT);

        assertThat(dto.getDestination()).isEqualTo("/lobbies/" + 1L + "/messages");
        assertThat(dto.getData()).isInstanceOf(MessageResponseDto.class);
    }

    @Test
    @DisplayName("시스템 메시지 생성/전송")
    void sendSystemMessage() {
        // given
        String content = "content";

        Lobby lobby = Lobby.createLobby("lobby", User.createGuest(""), null, null);

        when(lobbyRepository.findById(anyLong())).thenReturn(Optional.of(lobby));

        // when
        SendAfterCommitDto<MessageResponseDto> dto = messageService.sendSystemMessage(content, 1L);

        // then
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository, times(1)).save(captor.capture());

        Message message = captor.getValue();
        assertThat(message.getContent()).isEqualTo(content);
        assertThat(message.getLobby()).isEqualTo(lobby);
        assertThat(message.getType()).isEqualTo(MessageType.SYSTEM);

        assertThat(dto.getDestination()).isEqualTo("/lobbies/" + 1L + "/messages");
        assertThat(dto.getData()).isInstanceOf(MessageResponseDto.class);
    }
}