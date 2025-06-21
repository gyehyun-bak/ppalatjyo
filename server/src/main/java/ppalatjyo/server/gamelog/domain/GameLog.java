package ppalatjyo.server.gamelog.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.usergame.UserGame;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class GameLog extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Enumerated(EnumType.STRING)
    private GameLogType type;


    private static GameLog create(Game game, UserGame userGame, Message message, Question question, GameLogType type) {
        return GameLog.builder()
                .game(game)
                .userGame(userGame)
                .message(message)
                .question(question)
                .type(type)
                .build();
    }

    public static GameLog started(Game game) {
        return create(game, null, null, null, GameLogType.GAME_STARTED);
    }

    public static GameLog ended(Game game) {
        return create(game, null, null, null, GameLogType.GAME_ENDED);
    }

    public static GameLog rightAnswer(Game game, UserGame userGame, Message message) {
        return create(game, userGame, message, game.getCurrentQuestion(), GameLogType.RIGHT_ANSWER);
    }

    public static GameLog wrongAnswer(Game game, UserGame userGame, Message message) {
        return create(game, userGame, message, game.getCurrentQuestion(), GameLogType.WRONG_ANSWER);
    }

    public static GameLog timeOut(Game game) {
        return create(game, null, null, game.getCurrentQuestion(), GameLogType.TIME_OUT);
    }
}
