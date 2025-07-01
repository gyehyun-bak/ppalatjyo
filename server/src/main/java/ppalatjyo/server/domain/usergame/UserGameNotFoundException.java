package ppalatjyo.server.domain.usergame;

import java.util.NoSuchElementException;

public class UserGameNotFoundException extends NoSuchElementException {
    public UserGameNotFoundException() {
        super("UserGame not found");
    }
}
