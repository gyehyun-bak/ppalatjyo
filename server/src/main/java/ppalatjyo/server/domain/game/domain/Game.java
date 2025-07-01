package ppalatjyo.server.domain.game.domain;


import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.domain.game.exception.GameAlreadyEndedException;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.usergame.UserGame;

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

    @Builder.Default
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
