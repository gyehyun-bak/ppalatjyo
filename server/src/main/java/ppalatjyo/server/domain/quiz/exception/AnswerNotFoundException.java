package ppalatjyo.server.domain.quiz.exception;

import java.util.NoSuchElementException;

public class AnswerNotFoundException extends NoSuchElementException {
    public AnswerNotFoundException() {
        super("Answer not found");
    }
}
