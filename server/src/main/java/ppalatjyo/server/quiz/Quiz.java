package ppalatjyo.server.quiz;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Quiz {

    @Id @GeneratedValue
    private Long id;

    public static Quiz createQuiz(String quiz) {
        return new Quiz();
    }
}
