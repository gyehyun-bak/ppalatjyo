package ppalatjyo.server.quiz.exception;

public class QuestionAlreadyDeletedException extends RuntimeException {
    public QuestionAlreadyDeletedException() {
        super("Question already deleted");
    }
}
