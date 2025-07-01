package ppalatjyo.server.domain.userlobby.exception;

public class WrongLobbyPasswordException extends RuntimeException {
    public WrongLobbyPasswordException() {
        super("Wrong lobby password.");
    }
}
