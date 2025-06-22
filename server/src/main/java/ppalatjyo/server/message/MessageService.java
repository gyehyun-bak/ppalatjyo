package ppalatjyo.server.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.exception.LobbyNotFoundException;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.event.ChatMessageSentEvent;
import ppalatjyo.server.message.event.SystemMessageSentEvent;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.user.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void sendChatMessage(String content, Long userId, Long lobbyId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);

        Message message = Message.chatMessage(content, user, lobby);
        messageRepository.save(message);

        // MessageSentEventHandler 에서 트랜잭션 여부에 따라 분기 처리
        ChatMessageSentEvent event = new ChatMessageSentEvent(message.getId());
        eventPublisher.publishEvent(event);
    }

    public void sendSystemMessage(String content, Long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);

        Message message = Message.systemMessage(content, lobby);
        messageRepository.save(message);

        SystemMessageSentEvent event = new SystemMessageSentEvent(message.getId());
        eventPublisher.publishEvent(event);
    }
}
