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
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public static Quiz createQuiz(String name, User user) {
        if (user.getRole() == UserRole.GUEST) {
            throw new GuestCannotCreateQuizException();
        }

        return Quiz.builder()
                .name(name)
                .user(user)
                .questions(new ArrayList<>())
                .build();
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void addQuestion(Question question) {
        if (questions.contains(question)) {
            return;
        }
        questions.add(question);
    }
}
