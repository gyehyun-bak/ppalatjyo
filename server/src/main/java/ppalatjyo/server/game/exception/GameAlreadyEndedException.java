package ppalatjyo.server.game.exception;

public class GameAlreadyEndedException extends RuntimeException {
    public GameAlreadyEndedException() {
        super("Game has already ended.");
    }
}
