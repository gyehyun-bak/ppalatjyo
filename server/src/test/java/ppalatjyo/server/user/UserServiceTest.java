package ppalatjyo.server.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.user.domain.UserRole;
import ppalatjyo.server.user.dto.JoinAsGuestResponseDto;
import ppalatjyo.server.user.dto.JoinAsMemberResponseDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("GUEST로 참가시 토큰 반환")
    void joinAsGuest() {
        // given
        String nickname = "nickname";

        // when
        JoinAsGuestResponseDto responseDto = userService.joinAsGuest(nickname);

        // then
        assertThat(responseDto.getAccessToken()).isNotNull();
        assertThat(responseDto.getRefreshToken()).isNotNull();
        // TODO: 2025-06-02 Token 검증 추가
    }

    @Test
    @DisplayName("MEMBER로 가입")
    void joinAsMember() {
        // given
        String nickname = "nickname";
        String oAuthEmail = "test@test.com";
        String oAuthProvider = "github";

        // when
        JoinAsMemberResponseDto responseDto = userService.joinAsMember(nickname, oAuthEmail, oAuthProvider);

        // then
        assertThat(responseDto.getAccessToken()).isNotNull();
        assertThat(responseDto.getRefreshToken()).isNotNull();
        // TODO: 2025-06-02 Token 검증 추가
    }

    @Test
    @DisplayName("GUEST를 MEMBER로 승격")
    void promoteGuestToMember() {
        // given
        User user = User.createGuest("nickname");
        Long id = 1L;
        String oAuthEmail = "test@test.com";
        String oAuthProvider = "github";
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        userService.promoteGuestToMember(id, oAuthEmail, oAuthProvider);

        // then
        assertThat(user.getRole()).isEqualTo(UserRole.MEMBER);
    }

    @Test
    @DisplayName("User nickname 변경")
    void changeNickname() {
        // given
        String oldNickname = "oldNickname";
        String newNickname = "newNickname";
        User user = User.createGuest(oldNickname);
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        userService.changeNickname(id, newNickname);

        // then
        assertThat(user.getNickname()).isEqualTo(newNickname);
    }

    @Test
    @DisplayName("User 삭제")
    void deleteUser() {
        // given
        Long id = 1L;
        User user = User.createGuest("guest");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        userService.deleteUser(id);

        // then
        assertThat(user.isDeleted()).isTrue();
    }
}