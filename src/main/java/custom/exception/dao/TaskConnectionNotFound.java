package custom.exception.dao;

public class TaskConnectionNotFound extends NotFoundException {
    public TaskConnectionNotFound() {
        super("TaskConnection not found");
    }

    public TaskConnectionNotFound(String message) {
        super(message);
    }

    public TaskConnectionNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
