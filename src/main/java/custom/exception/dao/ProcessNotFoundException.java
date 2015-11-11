package custom.exception.dao;

public class ProcessNotFoundException extends NotFoundException {
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
