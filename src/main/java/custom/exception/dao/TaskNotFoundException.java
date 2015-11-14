package custom.exception.dao;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException() {
        super("Task not found");
    }

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
