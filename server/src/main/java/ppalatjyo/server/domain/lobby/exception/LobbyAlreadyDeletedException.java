package ppalatjyo.server.domain.lobby.exception;

public class LobbyAlreadyDeletedException extends RuntimeException {
    public LobbyAlreadyDeletedException() {
        super("Can't delete already deleted lobby.");
    }
}
