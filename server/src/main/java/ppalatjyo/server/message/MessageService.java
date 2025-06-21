package ppalatjyo.server.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.user.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;

}
