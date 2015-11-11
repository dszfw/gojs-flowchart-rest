package custom.exception.dao;

import custom.exception.DropwizardExampleException;

public class ProcessNotFoundException extends DropwizardExampleException {
    public ProcessNotFoundException() {
        super("Process not found");
    }

    public ProcessNotFoundException(String message) {
        super(message);
    }

    public ProcessNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
