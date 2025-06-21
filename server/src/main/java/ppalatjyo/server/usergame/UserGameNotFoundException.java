package ppalatjyo.server.usergame;

import java.util.NoSuchElementException;

public class UserGameNotFoundException extends NoSuchElementException {
    public UserGameNotFoundException() {
        super("UserGame not found");
    }
}
