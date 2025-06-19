package ppalatjyo.server.quiz.exception;

public class GuestCannotCreateQuizException extends RuntimeException {
    public GuestCannotCreateQuizException() {
        super("Guest cannot create quiz.");
    }
}
