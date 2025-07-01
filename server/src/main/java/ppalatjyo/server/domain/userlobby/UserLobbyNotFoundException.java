package ppalatjyo.server.domain.userlobby;

import java.util.NoSuchElementException;

public class UserLobbyNotFoundException extends NoSuchElementException {
    public UserLobbyNotFoundException() {
        super("UserLobby not found");
    }
}
