package ppalatjyo.server.quiz.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.global.audit.BaseEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "question_id")
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Answer> answers = new HashSet<>();

    public static Question create(Quiz quiz, String content, Answer... answers) {
        Question question = Question.builder()
                .quiz(quiz)
                .content(content)
                .answers(new HashSet<>())
                .build();

        quiz.addQuestion(question);

        for (Answer answer : answers) {
            question.addAnswer(answer);
        }

        return question;
    }

    private void addAnswer(Answer answer) {
        answer.setQuestion(this);
        answers.add(answer);
    }

    public void changeContent(String newContent) {
        content = newContent;
    }
}
