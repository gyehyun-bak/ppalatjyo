package ppalatjyo.server.gameevent;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.message.Message;
import ppalatjyo.server.usergame.UserGame;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class GameEvent extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "game_event_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_game_id")
    private UserGame userGame;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;

    @Enumerated(EnumType.STRING)
    private GameEventType type;


    private static GameEvent create(Game game, UserGame userGame, Message message, GameEventType type) {
        return GameEvent.builder()
                .game(game)
                .userGame(userGame)
                .message(message)
                .type(type)
                .build();
    }

    public static GameEvent started(Game game, UserGame userGame) {
        return create(game, userGame, null, GameEventType.GAME_STARTED);
    }

    public static GameEvent ended(Game game) {
        return create(game, null, null, GameEventType.GAME_ENDED);
    }

    public static GameEvent wrongAnswer(Game game, UserGame userGame, Message message) {
        return create(game, userGame, message, GameEventType.WRONG_ANSWER);
    }

    public static GameEvent rightAnswer(Game game, UserGame userGame, Message message) {
        return create(game, userGame, message, GameEventType.RIGHT_ANSWER);
    }

    public static GameEvent timeOut(Game game) {
        return create(game, null, null, GameEventType.TIME_OUT);
    }
}
