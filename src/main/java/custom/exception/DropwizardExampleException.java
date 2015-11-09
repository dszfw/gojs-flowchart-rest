package custom.exception;

public class DropwizardExampleException extends RuntimeException {
    public DropwizardExampleException() {
    }

    public DropwizardExampleException(String message) {
        super(message);
    }

    public DropwizardExampleException(String message, Throwable cause) {
        super(message, cause);
    }
}
