package ppalatjyo.server.domain.lobby.domain;

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

    public static final int DEFAULT_MAX_USERS = 10;
    public static final int DEFAULT_MIN_PER_GAME = 5;
    public static final int DEFAULT_SEC_PER_QUESTION = 60;

    public static LobbyOptions createOptions(int maxUsers, int minPerGame, int secPerQuestion) {
        return LobbyOptions.builder()
                .maxUsers(maxUsers)
                .minPerGame(minPerGame)
                .secPerQuestion(secPerQuestion)
                .build();
    }

    public static LobbyOptions defaultOptions() {
        return LobbyOptions.builder()
                .maxUsers(DEFAULT_MAX_USERS)
                .minPerGame(DEFAULT_MIN_PER_GAME)
                .secPerQuestion(DEFAULT_SEC_PER_QUESTION)
                .build();
    }
}
