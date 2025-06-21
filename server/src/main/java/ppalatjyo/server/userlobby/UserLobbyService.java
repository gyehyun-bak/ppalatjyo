package ppalatjyo.server.userlobby;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.exception.LobbyNotFoundException;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.user.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserLobbyService {

    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    private final UserLobbyRepository userLobbyRepository;

    public void join(Long userId, Long lobbyId) { // TODO: 참가 메시지 전송
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);

        UserLobby userLobby = UserLobby.join(user, lobby);

        userLobbyRepository.save(userLobby);
    }

    public void leave(Long userId, Long lobbyId) { // TODO: 퇴장 메시지 전송
        UserLobby userLobby = userLobbyRepository.findByUserIdAndLobbyId(userId, lobbyId).orElseThrow(UserLobbyNotFoundException::new);
        userLobby.leave();
    }
}
