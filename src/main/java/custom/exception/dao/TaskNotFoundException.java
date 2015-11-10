package custom.exception.dao;

import custom.exception.DropwizardExampleException;

public class TaskNotFoundException extends DropwizardExampleException {
    public TaskNotFoundException() {
    }

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
