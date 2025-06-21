package ppalatjyo.server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.user.dto.JoinAsGuestResponseDto;
import ppalatjyo.server.user.dto.JoinAsMemberResponseDto;
import ppalatjyo.server.user.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public JoinAsGuestResponseDto joinAsGuest(String nickname) {
        User guest = User.createGuest(nickname);
        userRepository.save(guest);

        return new JoinAsGuestResponseDto("", "");
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
}
