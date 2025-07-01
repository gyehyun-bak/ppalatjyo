package ppalatjyo.server.domain.quiz.exception;

import java.util.NoSuchElementException;

public class QuizNotFoundException extends NoSuchElementException {
    public QuizNotFoundException() {
        super("Quiz not found");
    }
}
