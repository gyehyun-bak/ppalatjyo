package ppalatjyo.server.lobby.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Embeddable
public class LobbyOptions {
    private int maxUsers;
    private int minPerGame;
    private int secPerQuestion;

    public static LobbyOptions createOptions(int maxUsers, int minPerGame, int secPerQuestion) {
        return LobbyOptions.builder()
                .maxUsers(maxUsers)
                .minPerGame(minPerGame)
                .secPerQuestion(secPerQuestion)
                .build();
    }
}
