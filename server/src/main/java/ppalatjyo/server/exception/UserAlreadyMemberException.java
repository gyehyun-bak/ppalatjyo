package ppalatjyo.server.exception;

public class UserAlreadyMemberException extends RuntimeException {
    public UserAlreadyMemberException() {
        super("User is already a member.");
    }
}
