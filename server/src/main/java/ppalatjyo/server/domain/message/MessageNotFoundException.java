package ppalatjyo.server.domain.message;

import java.util.NoSuchElementException;

public class MessageNotFoundException extends NoSuchElementException {
    public MessageNotFoundException() {
        super("Message not found");
    }
}
