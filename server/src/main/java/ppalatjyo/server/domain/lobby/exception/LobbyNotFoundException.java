package ppalatjyo.server.domain.lobby.exception;

import java.util.NoSuchElementException;

public class LobbyNotFoundException extends NoSuchElementException {
    public LobbyNotFoundException() {
        super("Lobby not found");
    }
}
