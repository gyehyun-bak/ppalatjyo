package ppalatjyo.server.domain.quiz.exception;

public class QuestionAlreadyDeletedException extends RuntimeException {
    public QuestionAlreadyDeletedException() {
        super("Question already deleted");
    }
}
