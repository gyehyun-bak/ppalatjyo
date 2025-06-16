package ppalatjyo.server.game;

public class NoNextQuestionException extends RuntimeException {
    public NoNextQuestionException() {
        super("No more questions.");
    }
}
