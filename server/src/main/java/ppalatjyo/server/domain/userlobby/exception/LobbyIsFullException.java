package ppalatjyo.server.domain.userlobby.exception;

public class LobbyIsFullException extends RuntimeException {
    public LobbyIsFullException() {
        super("Lobby is full.");
    }
}
