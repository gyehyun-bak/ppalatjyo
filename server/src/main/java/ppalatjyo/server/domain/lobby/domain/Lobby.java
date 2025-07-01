package ppalatjyo.server.domain.lobby.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.domain.lobby.exception.LobbyAlreadyDeletedException;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.userlobby.UserLobby;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Lobby extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "lobby_id")
    private Long id;

    private String name;
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private LobbyStatus status = LobbyStatus.WAITING;

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
    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL)
    private List<UserLobby> userLobbies = new ArrayList<>();

    @OneToMany(mappedBy = "lobby")
    private List<Game> games;

    public static Lobby create(String name, User host, Quiz quiz, LobbyOptions options) {
        Lobby lobby = Lobby.builder()
                .name(name)
                .host(host)
                .quiz(quiz)
                .userLobbies(new ArrayList<>())
                .options(options)
                .build();

        UserLobby.join(host, lobby);

        return lobby;
    }

    public static Lobby withPassword(String name, String password, User host, Quiz quiz, LobbyOptions options) {
        Lobby lobby = Lobby.builder()
                .name(name)
                .password(password)
                .host(host)
                .quiz(quiz)
                .userLobbies(new ArrayList<>())
                .options(options)
                .build();

        UserLobby.join(host, lobby);

        return lobby;
    }

    public void delete() {
        if (deletedAt != null) {
            throw new LobbyAlreadyDeletedException();
        }

        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
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

    /**
     * Lobby에 참여한 유저가 남지 않았는지 확인합니다.
     * @return Lobby가 비었으면 true | 아니면 false
     */
    public boolean isEmpty() {
        return userLobbies.stream().allMatch(UserLobby::isLeft);
    }

    /**
     * 비밀번호가 설정된 채팅방인지 확인합니다.
     * @return true이면 비밀번호가 있음
     */
    public boolean isProtected() {
        return password != null && !password.isBlank();
    }

    /**
     * 입력한 비밀번호가 맞는지 확인합니다.
     * @param inputPassword 사용자가 입력한 비밀번호
     * @return 비밀번호가 일치하면 true
     */
    public boolean isCorrectPassword(String inputPassword) {
        return isProtected() && password.equals(inputPassword);
    }
}
