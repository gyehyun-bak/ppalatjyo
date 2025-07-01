package ppalatjyo.server.domain.usergame;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.domain.user.domain.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class UserGame extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_game_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    private int score;

    public static UserGame join(User user, Game game) {
        return UserGame.builder()
                .user(user)
                .game(game)
                .score(0)
                .build();
    }

    public void increaseScoreBy(int amount) {
        score += amount;
    }
}
