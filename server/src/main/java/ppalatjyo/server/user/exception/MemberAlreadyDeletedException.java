package ppalatjyo.server.user.exception;

public class MemberAlreadyDeletedException extends RuntimeException {
    public MemberAlreadyDeletedException() {
        super("Member is already deleted");
    }
}
