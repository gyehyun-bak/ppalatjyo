package ppalatjyo.server.domain.userlobby;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.domain.lobby.LobbyRepository;
import ppalatjyo.server.domain.lobby.LobbyService;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.lobby.exception.LobbyNotFoundException;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.user.exception.UserNotFoundException;
import ppalatjyo.server.domain.userlobby.dto.JoinLobbyRequestDto;
import ppalatjyo.server.domain.userlobby.event.UserJoinedLobbyEvent;
import ppalatjyo.server.domain.userlobby.event.UserLeftLobbyEvent;
import ppalatjyo.server.domain.userlobby.exception.LobbyIsFullException;
import ppalatjyo.server.domain.userlobby.exception.UserLobbyNotFoundException;
import ppalatjyo.server.domain.userlobby.exception.WrongLobbyPasswordException;

/**
 * UserLobby 도메인의 상태와 생명 주기를 관리합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserLobbyService {

    private final UserRepository userRepository;
    private final LobbyRepository lobbyRepository;
    private final UserLobbyRepository userLobbyRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final LobbyService lobbyService;

    /**
     * Lobby에 User가 참가합니다.
     * <p>비밀번호가 설정된 Lobby에 제공된 비밀번호가 맞지 않는 경우 {@link WrongLobbyPasswordException}을 던집니다.
     * <p>Lobby가 이미 가득찬 경우 {@link LobbyIsFullException}을 던집니다.
     */
    public void join(JoinLobbyRequestDto requestDto) throws LobbyIsFullException, WrongLobbyPasswordException {
        long userId = requestDto.getUserId();
        long lobbyId = requestDto.getLobbyId();
        String password = requestDto.getPassword();

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);

        if (lobby.isProtected()) {
            if (password == null || !lobby.isCorrectPassword(password)) {
                throw new WrongLobbyPasswordException();
            }
        }

        boolean alreadyJoined = userLobbyRepository.existsByUserIdAndLobbyIdAndLeftAtIsNull(userId, lobbyId);
        if (alreadyJoined) {
            return; // 이미 참가 중인 유저인 경우 무시합니다.
        }

        int currentTotalUsers = userLobbyRepository.countByLobbyIdAndLeftAtIsNull(lobbyId);
        if (currentTotalUsers == lobby.getOptions().getMaxUsers()) {
            throw new LobbyIsFullException();
        }

        UserLobby userLobby = UserLobby.join(user, lobby);

        userLobbyRepository.save(userLobby);

        applicationEventPublisher.publishEvent(new UserJoinedLobbyEvent(userId, lobbyId));
    }

    /**
     * <p> User가 Lobby를 나갑니다. User가 나가고 더 이상 Lobby에 참가한 인원이 없으면 Lobby를 삭제합니다.
     * <p> Lobby에서 실행되는 게임을 모두 종료합니다. (진행 중인 게임이 있는 경우 끝까지 진행되지 않게 하기 위함)
     */
    public void leave(Long userId, Long lobbyId) {
        UserLobby userLobby = userLobbyRepository.findByUserIdAndLobbyId(userId, lobbyId).orElseThrow(UserLobbyNotFoundException::new);
        leave(userLobby);
        userLobbyRepository.flush(); // 삭제 반영을 위해 flush() 합니다.

        // 로비가 비었으면 게임 종료 후 삭제
        lobbyService.deleteIfEmpty(lobbyId);
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
