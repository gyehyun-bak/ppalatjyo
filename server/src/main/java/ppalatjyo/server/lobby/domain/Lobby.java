package ppalatjyo.server.lobby.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.lobby.LobbyAlreadyDeletedException;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Lobby extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "lobby_id")
    private Long id;

    private String name;

    @Embedded
    private LobbyOptions options;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public static Lobby createLobby(String name, User host, Quiz quiz, LobbyOptions options) {
        return Lobby.builder()
                .name(name)
                .host(host)
                .quiz(quiz)
                .options(options)
                .build();
    }

    public void delete() {
        if (deletedAt != null) {
            throw new LobbyAlreadyDeletedException();
        }

        deletedAt = LocalDateTime.now();
    }

    public void changeHost(User newHost) {
        this.host = newHost;
    }

    public void changeOptions(LobbyOptions options) {
        this.options = options;
    }

    public void changeQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
