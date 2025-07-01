package ppalatjyo.server.domain.userlobby;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ppalatjyo.server.domain.message.MessageService;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.user.exception.UserNotFoundException;
import ppalatjyo.server.domain.userlobby.event.UserJoinedLobbyEvent;
import ppalatjyo.server.domain.userlobby.event.UserLeftLobbyEvent;

@Component
@RequiredArgsConstructor
public class UserLobbyEventHandler {

    private final MessageService messageService;
    private final UserRepository userRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserJoinedLobbyEvent(UserJoinedLobbyEvent event) {
        User user = userRepository.findById(event.getUserId()).orElseThrow(UserNotFoundException::new);
        String content = user.getNickname() + "님이 참가하였습니다.";
        messageService.sendSystemMessage(content, event.getLobbyId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserLeftLobbyEvent(UserLeftLobbyEvent event) {
        User user = userRepository.findById(event.getUserId()).orElseThrow(UserNotFoundException::new);
        String content = user.getNickname() + "님이 나갔습니다.";
        messageService.sendSystemMessage(content, event.getLobbyId());
    }
}
