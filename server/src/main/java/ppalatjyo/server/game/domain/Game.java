package ppalatjyo.server.game.domain;


import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ppalatjyo.server.game.exception.GameAlreadyEndedException;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.usergame.UserGame;
import ppalatjyo.server.userlobby.UserLobby;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Game extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "game_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGame> userGames = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_question_id")
    private Question currentQuestion;

    private int currentQuestionIndex;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @Embedded
    private GameOptions options;

    public static Game start(Lobby lobby) {
        Game game = Game.builder()
                .lobby(lobby)
                .quiz(lobby.getQuiz())
                .currentQuestion(lobby.getQuiz().getQuestions().getFirst())
                .currentQuestionIndex(0)
                .userGames(new ArrayList<>())
                .startedAt(LocalDateTime.now())
                .options(GameOptions.copy(lobby.getOptions()))
                .build();

        lobby.getUserLobbies()
                .forEach(userLobby -> {
                    UserGame userGame = UserGame.join(userLobby.getUser(), game);
                    game.getUserGames().add(userGame);
                });

        return game;
    }

    public void end() {
        if (isEnded()) {
            throw new GameAlreadyEndedException();
        }

        endedAt = LocalDateTime.now();
    }

    public boolean isEnded() {
        return endedAt != null;
    }

    public boolean hasNextQuestion() {
        return quiz.getQuestions().size() > 1 && currentQuestionIndex < quiz.getQuestions().size() - 1;
    }

    public void nextQuestion() {
        if (!hasNextQuestion()) {
            end();
            return;
        }

        currentQuestion = quiz.getQuestions().get(++currentQuestionIndex);
    }
}
