package custom.dto.task;

import custom.domain.Task;

public class TaskDTOinProcessDTO {

    private Long id;
    private Long position;

    public TaskDTOinProcessDTO() {
    }

    public TaskDTOinProcessDTO(Task task, Long position) {
        this.id = task.getId();
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }
}
