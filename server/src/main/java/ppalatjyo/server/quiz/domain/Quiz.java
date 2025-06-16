package ppalatjyo.server.quiz.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.quiz.GuestCannotCreateQuizException;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.user.domain.UserRole;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public static Quiz createQuiz(String title, User user) {
        if (user.getRole() == UserRole.GUEST) {
            throw new GuestCannotCreateQuizException();
        }

        return Quiz.builder()
                .title(title)
                .user(user)
                .questions(new ArrayList<>())
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
}
