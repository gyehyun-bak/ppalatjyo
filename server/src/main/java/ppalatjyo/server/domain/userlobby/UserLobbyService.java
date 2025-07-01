package ppalatjyo.server.domain.userlobby;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.lobby.LobbyRepository;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.lobby.exception.LobbyNotFoundException;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.user.exception.UserNotFoundException;
import ppalatjyo.server.domain.userlobby.event.UserJoinedLobbyEvent;
import ppalatjyo.server.domain.userlobby.event.UserLeftLobbyEvent;

@Service
@RequiredArgsConstructor
@Transactional
public class UserLobbyService {

    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    private final UserLobbyRepository userLobbyRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void join(Long userId, Long lobbyId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);

        UserLobby userLobby = UserLobby.join(user, lobby);

        userLobbyRepository.save(userLobby);

        applicationEventPublisher.publishEvent(new UserJoinedLobbyEvent(userId, lobbyId));
    }

    public void leave(Long userId, Long lobbyId) {
        UserLobby userLobby = userLobbyRepository.findByUserIdAndLobbyId(userId, lobbyId).orElseThrow(UserLobbyNotFoundException::new);
        leave(userLobby);
    }

    public void leaveAllLobbies(Long userId) {
        userLobbyRepository.findByUserId(userId)
                .forEach(this::leave);
    }

    private void leave(UserLobby userLobby) {
        userLobby.leave();

        applicationEventPublisher.publishEvent(new UserLeftLobbyEvent(userLobby.getUser().getId(), userLobby.getLobby().getId()));
    }
}
