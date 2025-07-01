package ppalatjyo.server.domain.quiz.exception;

public class AnswerAlreadyDeletedException extends RuntimeException {
    public AnswerAlreadyDeletedException() {
        super("Answer already deleted");
    }
}
