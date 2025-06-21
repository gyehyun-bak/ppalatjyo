package ppalatjyo.server.quiz.exception;

import java.util.NoSuchElementException;

public class QuestionNotFoundException extends NoSuchElementException {
    public QuestionNotFoundException() {
        super("Question not found");
    }
}
