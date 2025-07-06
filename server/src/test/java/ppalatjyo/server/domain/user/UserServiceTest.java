package ppalatjyo.server.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.UserService;
import ppalatjyo.server.domain.user.domain.OAuthProvider;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.user.domain.UserRole;
import ppalatjyo.server.domain.user.dto.JoinAsMemberResponseDto;
import ppalatjyo.server.domain.userlobby.UserLobbyService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLobbyService userLobbyService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("GUEST로 참가 시 userId 반환")
    void joinAsGuest() {
        // given
        String nickname = "nickname";
        User saved = mock(User.class);
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(saved.getId()).thenReturn(1L);

        // when
        long userId = userService.joinAsGuest(nickname);

        // then
        assertThat(userId).isEqualTo(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("MEMBER로 가입")
    void joinAsMember() {
        // given
        String nickname = "nickname";
        String oAuthEmail = "test@test.com";
        OAuthProvider oAuthProvider = OAuthProvider.GITHUB;

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
        OAuthProvider oAuthProvider = OAuthProvider.GITHUB;
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

    @Test
    @DisplayName("연결 끊김")
    void disconnect() {
        // given
        Long id = 1L;
        User user = User.createGuest("guest");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        userService.disconnect(id);

        // then
        verify(userLobbyService, times(1)).leaveAllLobbies(user.getId());
    }
}