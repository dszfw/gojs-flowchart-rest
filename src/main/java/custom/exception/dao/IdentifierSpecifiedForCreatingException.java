package custom.exception.dao;

import custom.exception.DropwizardExampleException;

public class IdentifierSpecifiedForCreatingException extends DropwizardExampleException {

    public IdentifierSpecifiedForCreatingException() {
    }

    public IdentifierSpecifiedForCreatingException(String message) {
        super(message);
    }

    public IdentifierSpecifiedForCreatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
