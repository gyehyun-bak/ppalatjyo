package ppalatjyo.server.domain.game.exception;

public class NoNextQuestionException extends RuntimeException {
    public NoNextQuestionException() {
        super("No more questions.");
    }
}
