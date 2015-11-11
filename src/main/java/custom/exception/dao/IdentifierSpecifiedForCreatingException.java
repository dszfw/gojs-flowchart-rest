package custom.exception.dao;

public class IdentifierSpecifiedForCreatingException extends RuntimeException {

    public IdentifierSpecifiedForCreatingException() {
    }

    public IdentifierSpecifiedForCreatingException(String message) {
        super(message);
    }

    public IdentifierSpecifiedForCreatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
