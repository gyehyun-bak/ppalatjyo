package ppalatjyo.server.user;

public class UserAlreadyMemberException extends RuntimeException {
    public UserAlreadyMemberException() {
        super("User is already a member.");
    }
}
