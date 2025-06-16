package ppalatjyo.server.quiz;

public class GuestCannotCreateQuizException extends RuntimeException {
    public GuestCannotCreateQuizException() {
        super("Guest cannot create quiz.");
    }
}
