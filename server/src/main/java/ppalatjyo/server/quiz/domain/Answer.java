package ppalatjyo.server.quiz.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {
    @Id @GeneratedValue
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public static Answer createAnswer(String content) {
        return Answer.builder()
                .content(content)
                .build();
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
