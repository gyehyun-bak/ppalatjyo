package ppalatjyo.server.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.exception.UserAlreadyMemberException;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String nickname;
    private String oAuthEmail;
    private String oAuthProvider;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime lastModifiedAt = LocalDateTime.now();

    private LocalDateTime deletedAt;

    @Builder.Default
    private LocalDateTime lastAccessedAt = LocalDateTime.now();


    public static User createGuest(String nickname) {
        return User.builder()
                .nickname(nickname)
                .role(UserRole.GUEST)
                .build();
    }

    public static User createMember(String nickname, String oAuthEmail, String oAuthProvider) {
        return User.builder()
                .nickname(nickname)
                .role(UserRole.MEMBER)
                .oAuthEmail(oAuthEmail)
                .oAuthProvider(oAuthProvider)
                .build();
    }

    public void promoteGuestToMember(String oAuthEmail, String oAuthProvider) {
        if (this.role == UserRole.MEMBER || this.role == UserRole.ADMIN) {
            throw new UserAlreadyMemberException();
        }

        this.oAuthEmail = oAuthEmail;
        this.oAuthProvider = oAuthProvider;
        this.role = UserRole.MEMBER;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void changeNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname must not be null or empty");
        }

        this.nickname = nickname;
    }
}
