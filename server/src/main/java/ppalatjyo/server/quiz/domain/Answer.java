package ppalatjyo.server.quiz.domain;

import jakarta.persistence.*;
import lombok.*;
import ppalatjyo.server.global.audit.BaseEntity;
import ppalatjyo.server.quiz.exception.AnswerAlreadyDeletedException;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "answer_id")
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private LocalDateTime deletedAt;

    public static Answer createAnswer(Question question, String content) {
        Answer answer = Answer.builder()
                .content(content)
                .question(question)
                .build();

        question.addAnswer(answer);

        return answer;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void delete() {
        if (isDeleted()) {
            throw new AnswerAlreadyDeletedException();
        }
        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isCorrect(String submission) {
        return content.equalsIgnoreCase(submission);
    }
}
