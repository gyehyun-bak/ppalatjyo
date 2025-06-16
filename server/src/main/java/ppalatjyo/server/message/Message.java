package ppalatjyo.server.message;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.game.Game;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.user.domain.User;

import java.lang.reflect.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    public static Message create(String content, User user, Lobby lobby) {
        return create(content, user, lobby, null);
    }

    public static Message create(String content, User user, Lobby lobby, Game game) {
        return Message.builder()
                .content(content)
                .user(user)
                .lobby(lobby)
                .game(game)
                .build();
    }
}
