package ppalatjyo.server.quiz.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {
    @Id @GeneratedValue
    @Column(name = "question_id")
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Answer> answers = new HashSet<>();

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public static Question createQuestion(String content, Answer... answers) {
        Question question = Question.builder()
                .content(content)
                .answers(new HashSet<>())
                .build();

        for (Answer answer : answers) {
            question.addAnswer(answer);
        }

        return question;
    }

    private void addAnswer(Answer answer) {
        answer.setQuestion(this);
        answers.add(answer);
    }
}
