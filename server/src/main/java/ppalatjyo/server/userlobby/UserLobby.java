package ppalatjyo.server.userlobby;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserLobby {
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
        return UserLobby.builder()
                .user(user)
                .lobby(lobby)
                .joinedAt(LocalDateTime.now())
                .build();
    }

    public void leave() {
        this.leftAt = LocalDateTime.now();
    }
}
