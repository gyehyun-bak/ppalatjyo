package ppalatjyo.server.lobby;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import ppalatjyo.server.quiz.Quiz;
import ppalatjyo.server.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Lobby {

    @Id @GeneratedValue
    private Long id;
    private int maxUsers;
    private int timePerGame;
    private int timePerQuestion;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime lastModifiedAt = LocalDateTime.now();

    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public static Lobby createLobby(User host, Quiz quiz) {
        return Lobby.builder()
                .host(host)
                .quiz(quiz)
                .build();
    }

    public static Lobby createLobbyWithOptions(User host, Quiz quiz, int maxUsers, int timePerGame, int timePerQuestion) {
        return Lobby.builder()
                .host(host)
                .quiz(quiz)
                .maxUsers(maxUsers)
                .timePerGame(timePerGame)
                .timePerQuestion(timePerQuestion)
                .build();
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }
}
