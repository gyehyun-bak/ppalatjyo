package ppalatjyo.server.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.domain.user.exception.UserAlreadyMemberException;
import ppalatjyo.server.domain.user.exception.MemberAlreadyDeletedException;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String nickname;
    private String oAuthEmail;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    private LocalDateTime deletedAt;

    @Builder.Default
    private LocalDateTime lastAccessedAt = LocalDateTime.now();

    public static User createGuest(String nickname) {
        return User.builder()
                .nickname(nickname)
                .role(UserRole.GUEST)
                .build();
    }

    public static User createMember(String nickname, String oAuthEmail, OAuthProvider provider) {
        return User.builder()
                .nickname(nickname)
                .role(UserRole.MEMBER)
                .oAuthEmail(oAuthEmail)
                .oAuthProvider(provider)
                .build();
    }

    public void promoteGuestToMember(String oAuthEmail, OAuthProvider provider) {
        if (this.role == UserRole.MEMBER || this.role == UserRole.ADMIN) {
            throw new UserAlreadyMemberException();
        }

        this.oAuthEmail = oAuthEmail;
        this.oAuthProvider = provider;
        this.role = UserRole.MEMBER;
    }

    public void changeNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname must not be null or empty");
        }

        this.nickname = nickname;
    }

    public void access() {
        this.lastAccessedAt = LocalDateTime.now();
    }

    public void delete() {
        if (isDeleted()) {
            throw new MemberAlreadyDeletedException();
        }

        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
