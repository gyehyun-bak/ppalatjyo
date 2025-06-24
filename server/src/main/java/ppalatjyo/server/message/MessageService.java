package ppalatjyo.server.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.global.websocket.aop.SendAfterCommit;
import ppalatjyo.server.global.websocket.aop.SendAfterCommitDto;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.exception.LobbyNotFoundException;
import ppalatjyo.server.message.domain.Message;
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

    @SendAfterCommit
    public SendAfterCommitDto<MessageResponseDto> sendChatMessage(String content, Long userId, Long lobbyId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);

        Message message = Message.chatMessage(content, user, lobby);
        messageRepository.save(message);

        return new SendAfterCommitDto<>("/lobbies/" + lobbyId + "/messages", MessageResponseDto.chatMessage(message));
    }

    @SendAfterCommit
    public SendAfterCommitDto<MessageResponseDto> sendSystemMessage(String content, Long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);

        Message message = Message.systemMessage(content, lobby);
        messageRepository.save(message);

        return new SendAfterCommitDto<>("/lobbies/" + lobbyId + "/messages", MessageResponseDto.systemMessage(message));
    }
}
