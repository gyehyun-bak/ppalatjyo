package ppalatjyo.server.domain.quiz.exception;

import java.util.NoSuchElementException;

public class QuestionNotFoundException extends NoSuchElementException {
    public QuestionNotFoundException() {
        super("Question not found");
    }
}
