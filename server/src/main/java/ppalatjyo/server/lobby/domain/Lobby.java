package ppalatjyo.server.lobby.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.lobby.exception.LobbyAlreadyDeletedException;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.userlobby.UserLobby;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Builder.Default
    @OneToMany(mappedBy = "lobby", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLobby> userLobbies = new ArrayList<>();

    public static Lobby createLobby(String name, User host, Quiz quiz, LobbyOptions options) {
        Lobby lobby = Lobby.builder()
                .name(name)
                .host(host)
                .quiz(quiz)
                .userLobbies(new ArrayList<>())
                .options(options)
                .build();

        UserLobby userLobby = UserLobby.join(host, lobby);

        return lobby;
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
