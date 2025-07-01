package ppalatjyo.server.domain.quiz.exception;

public class QuizAlreadyDeletedException extends RuntimeException {
    public QuizAlreadyDeletedException() {
        super("Quiz already deleted");
    }
}
