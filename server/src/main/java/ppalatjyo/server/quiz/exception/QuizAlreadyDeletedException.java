package ppalatjyo.server.quiz.exception;

public class QuizAlreadyDeletedException extends RuntimeException {
    public QuizAlreadyDeletedException() {
        super("Quiz already deleted");
    }
}
