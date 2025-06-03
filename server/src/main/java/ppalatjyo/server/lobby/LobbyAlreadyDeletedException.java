package ppalatjyo.server.lobby;

public class LobbyAlreadyDeletedException extends RuntimeException {
    public LobbyAlreadyDeletedException() {
        super("Can't delete already deleted lobby.");
    }
}
