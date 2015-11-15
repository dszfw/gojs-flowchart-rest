package custom.exception.dao;

import custom.exception.DropwizardExampleException;

public class TaskNotAssociatedWithProcess extends DropwizardExampleException {

    public TaskNotAssociatedWithProcess() {
        super("Task did not associated with process");
    }

    public TaskNotAssociatedWithProcess(String message) {
        super(message);
    }

    public TaskNotAssociatedWithProcess(String message, Throwable cause) {
        super(message, cause);
    }
}
