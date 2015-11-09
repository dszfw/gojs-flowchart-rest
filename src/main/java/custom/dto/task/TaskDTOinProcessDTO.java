package custom.dto.task;

import custom.domain.Task;

public class TaskDTOinProcessDTO {
    private long id;
    private long position;

    public TaskDTOinProcessDTO() {
    }

    public TaskDTOinProcessDTO(Task task, long position) {
        this.id = task.getId();
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
