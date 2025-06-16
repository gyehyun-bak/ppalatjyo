package ppalatjyo.server.game;

import jakarta.persistence.Embeddable;
import lombok.*;
import ppalatjyo.server.lobby.domain.LobbyOptions;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Embeddable
public class GameOptions {
    private int minPerGame;
    private int secPerQuestion;

    public static GameOptions copy(LobbyOptions options) {
        return GameOptions.builder()
                .minPerGame(options.getMinPerGame())
                .secPerQuestion(options.getSecPerQuestion())
                .build();
    }
}
