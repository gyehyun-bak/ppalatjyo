package ppalatjyo.server.domain.game.exception;

import java.util.NoSuchElementException;

public class GameNotFoundException extends NoSuchElementException {
    public GameNotFoundException() {
        super("Game not found");
    }
}
