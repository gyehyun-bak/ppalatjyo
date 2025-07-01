package ppalatjyo.server.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.domain.user.exception.UserAlreadyMemberException;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.user.domain.UserRole;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    @DisplayName("GUEST User 생성")
    void createGuestUser() {
        // given
        String nickname = "nickname";

        // when
        User user = User.createGuest(nickname);

        // then
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getRole()).isEqualTo(UserRole.GUEST);
        assertThat(user.getLastAccessedAt()).isNotNull();
        assertThat(user.getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("MEMBER User 생성")
    void createMemberUser() {
        // given
        String nickname = "nickname";
        String oAuthEmail = "member@test.com";
        String oAuthProvider = "provider";

        // when
        User user = User.createMember(nickname, oAuthEmail, oAuthProvider);

        // then
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getRole()).isEqualTo(UserRole.MEMBER);
        assertThat(user.getLastAccessedAt()).isNotNull();
        assertThat(user.getDeletedAt()).isNull();

        assertThat(user.getOAuthEmail()).isEqualTo(oAuthEmail);
        assertThat(user.getOAuthProvider()).isEqualTo(oAuthProvider);
    }

    @Test
    @DisplayName("GUEST를 MEMBER로 승격")
    void promoteGuestToMember() {
        // given
        String nickname = "nickname";
        User user = User.createGuest(nickname);

        String oAuthEmail = "member@test.com";
        String oAuthProvider = "provider";

        // when
        user.promoteGuestToMember(oAuthEmail, oAuthProvider);

        // then
        assertThat(user.getRole()).isEqualTo(UserRole.MEMBER);
        assertThat(user.getOAuthEmail()).isEqualTo(oAuthEmail);
        assertThat(user.getOAuthProvider()).isEqualTo(oAuthProvider);
    }

    @Test
    @DisplayName("이미 MEMBER인 경우 승격 시 예외 발생")
    void promoteMemberToMemberThrowsException() {
        // given
        String nickname = "nickname";
        String oAuthEmail = "member@test.com";
        String oAuthProvider = "provider";

        User user = User.createMember(nickname, oAuthEmail, oAuthProvider);

        // when
        // then
        assertThatThrownBy(() -> user.promoteGuestToMember(oAuthEmail, oAuthProvider))
                .isInstanceOf(UserAlreadyMemberException.class);
    }

    @Test
    @DisplayName("유저는 nickname 변경 가능")
    void editNickname() {
        // given
        String nickname = "nickname";
        String newNickname = "newNickname";

        User user = User.createGuest(nickname);

        // when
        user.changeNickname(newNickname);

        // then
        assertThat(user.getNickname()).isEqualTo(newNickname);
    }

    @Test
    @DisplayName("nickname 변경 시 null 혹은 빈 값이면 예외 발생")
    void editNicknameNullThrowsException() {
        // given
        String nickname = "nickname";
        User user = User.createGuest(nickname);

        // when
        // then
        assertThatThrownBy(() -> user.changeNickname(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> user.changeNickname(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("접속한 유저는 마지막 접속 시간 업데이트")
    void access() throws InterruptedException {
        // given
        String nickname = "nickname";
        User user = User.createGuest(nickname);
        LocalDateTime lastAccessedAtBefore = user.getLastAccessedAt();
        Thread.sleep(10);

        // when
        user.access();

        // then
        assertThat(user.getLastAccessedAt()).isNotEqualTo(lastAccessedAtBefore);
    }

    @Test
    @DisplayName("User 삭제")
    void delete() {
        // given
        User user = User.createGuest("nickname");

        // when
        user.delete();

        // then
        assertThat(user.isDeleted()).isTrue();
        assertThat(user.getDeletedAt()).isNotNull();
    }
}