package ppalatjyo.server.domain.userlobby.exception;

public class LobbyIsInGameException extends RuntimeException{
    public LobbyIsInGameException() {
        super("Lobby is in game.");
    }
}
