package ppalatjyo.server.domain.user.exception;

public class UserAlreadyMemberException extends RuntimeException {
    public UserAlreadyMemberException() {
        super("User is already a member.");
    }
}
