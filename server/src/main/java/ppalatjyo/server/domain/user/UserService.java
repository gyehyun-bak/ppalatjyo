package ppalatjyo.server.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.user.dto.JoinAsMemberResponseDto;
import ppalatjyo.server.domain.user.exception.UserNotFoundException;
import ppalatjyo.server.domain.userlobby.UserLobbyService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserLobbyService userLobbyService;

    public long joinAsGuest(String nickname) {
        User guest = User.createGuest(nickname);
        User saved = userRepository.save(guest);
        return saved.getId();
    }

    public JoinAsMemberResponseDto joinAsMember(String nickname, String oAuthEmail, String oAuthProvider) {
        User member = User.createMember(nickname, oAuthEmail, oAuthProvider);
        userRepository.save(member);

        return new JoinAsMemberResponseDto("", "");
    }

    public void promoteGuestToMember(Long userId, String oAuthEmail, String oAuthProvider) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.promoteGuestToMember(oAuthEmail, oAuthProvider);
    }

    public void changeNickname(Long userId, String newNickname) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changeNickname(newNickname);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.delete();
    }

    public void disconnect(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userLobbyService.leaveAllLobbies(user.getId());
    }
}
