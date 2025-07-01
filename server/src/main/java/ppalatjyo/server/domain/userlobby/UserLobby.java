package ppalatjyo.server.domain.userlobby;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserLobby extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_lobby_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;

    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;

    public static UserLobby join(User user, Lobby lobby) {
        UserLobby userLobby = UserLobby.builder()
                .user(user)
                .lobby(lobby)
                .joinedAt(LocalDateTime.now())
                .build();

        lobby.getUserLobbies().add(userLobby);

        return userLobby;
    }

    public void leave() {
        this.leftAt = LocalDateTime.now();
    }

    public boolean isLeft() {
        return this.leftAt != null;
    }
}
