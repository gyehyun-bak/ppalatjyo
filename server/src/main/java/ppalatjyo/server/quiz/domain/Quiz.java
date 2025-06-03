package ppalatjyo.server.quiz.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz extends BaseEntity {

    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public static Quiz createQuiz(String name) {
        return Quiz.builder()
                .name(name)
                .questions(new ArrayList<>())
                .build();
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void addQuestion(Question question) {
        question.setQuiz(this);
        questions.add(question);
    }
}
