package ppalatjyo.server.domain.userlobby.exception;

import java.util.NoSuchElementException;

public class UserLobbyNotFoundException extends NoSuchElementException {
    public UserLobbyNotFoundException() {
        super("UserLobby not found");
    }
}
