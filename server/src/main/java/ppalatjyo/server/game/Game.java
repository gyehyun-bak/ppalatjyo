package ppalatjyo.server.game;


import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.game.exception.GameAlreadyEndedException;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;

import java.time.LocalDateTime;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_question_id")
    private Question currentQuestion;

    private int currentQuestionIndex;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @Embedded
    private GameOptions options;

    public static Game start(Lobby lobby) {
        return Game.builder()
                .lobby(lobby)
                .quiz(lobby.getQuiz())
                .currentQuestion(lobby.getQuiz().getQuestions().getFirst())
                .currentQuestionIndex(0)
                .startedAt(LocalDateTime.now())
                .options(GameOptions.copy(lobby.getOptions()))
                .build();
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
