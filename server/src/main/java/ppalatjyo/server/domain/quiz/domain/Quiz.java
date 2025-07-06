package ppalatjyo.server.domain.quiz.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.domain.quiz.exception.GuestCannotCreateQuizException;
import ppalatjyo.server.domain.quiz.exception.QuestionAlreadyDeletedException;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.user.domain.UserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "quiz_id")
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private QuizVisibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    private LocalDateTime deletedAt;

    public static Quiz createQuiz(String title, User user) {
        if (user.getRole() == UserRole.GUEST) {
            throw new GuestCannotCreateQuizException();
        }

        return Quiz.builder()
                .title(title)
                .user(user)
                .questions(new ArrayList<>())
                .visibility(QuizVisibility.PRIVATE)
                .build();
    }

    public static Quiz createQuiz(String title, User user, String description, QuizVisibility visibility) {
        if (user.getRole() == UserRole.GUEST) {
            throw new GuestCannotCreateQuizException();
        }

        return Quiz.builder()
                .title(title)
                .user(user)
                .description(description)
                .questions(new ArrayList<>())
                .visibility(visibility)
                .build();
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void addQuestion(Question question) {
        if (questions.contains(question)) {
            return;
        }
        questions.add(question);
    }

    public void delete() {
        if (isDeleted()) {
            throw new QuestionAlreadyDeletedException();
        }
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
