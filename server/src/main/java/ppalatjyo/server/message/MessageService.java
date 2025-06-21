package ppalatjyo.server.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void sendChatMessage(String content, Long userId, Long lobbyId) {
        User user = userRepository.findById(userId).orElseThrow();
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow();

        Message message = Message.chatMessage(content, user, lobby);
        messageRepository.save(message);

        // MessageSentEventHandler 에서 트랜잭션 여부에 따라 분기 처리
        MessageSentEvent event = new MessageSentEvent(userId, lobbyId, message.getId());
        eventPublisher.publishEvent(event);
    }
}
