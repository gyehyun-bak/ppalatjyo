package ppalatjyo.server.domain.user.exception;

public class MemberAlreadyDeletedException extends RuntimeException {
    public MemberAlreadyDeletedException() {
        super("Member is already deleted");
    }
}
