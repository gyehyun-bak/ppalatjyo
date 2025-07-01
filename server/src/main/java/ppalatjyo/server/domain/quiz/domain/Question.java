package ppalatjyo.server.domain.quiz.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.domain.quiz.exception.QuestionAlreadyDeletedException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Quiz quiz;

    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers = new HashSet<>();

    private LocalDateTime deletedAt;

    public static Question create(Quiz quiz, String content) {
        Question question = Question.builder()
                .quiz(quiz)
                .content(content)
                .answers(new HashSet<>())
                .build();

        quiz.addQuestion(question);

        return question;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void changeContent(String newContent) {
        content = newContent;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void delete() {
        if (isDeleted()) {
            throw new QuestionAlreadyDeletedException();
        }
        deletedAt = LocalDateTime.now();
    }

    public boolean isCorrect(String submission) {
        for (Answer answer : answers) {
            return answer.isCorrect(submission);
        }

        return false;
    }
}
