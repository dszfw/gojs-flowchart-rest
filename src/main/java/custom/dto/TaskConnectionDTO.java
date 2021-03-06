package custom.dto;

import com.google.common.base.Optional;
import custom.domain.Process;
import custom.domain.*;

public class TaskConnectionDTO implements BaseDTO {
    private Long id;
    private String name;
    private Long fromTask;
    private Long toTask;
    private Long process;
    private String fromConnector;
    private String toConnector;

    public TaskConnectionDTO() {
    }

    public TaskConnectionDTO(TaskConnection connection) {
        this.id = connection.getId();
        this.name = connection.getName();
        Optional<Task> fromNullable = Optional.fromNullable(connection.getFromTask());
        Optional<Task> toNullable = Optional.fromNullable(connection.getToTask());
        this.fromTask = fromNullable.isPresent() ? fromNullable.get().getId() : null;
        this.toTask = toNullable.isPresent() ? toNullable.get().getId() : null;
        Optional<Process> processNullable1 = Optional.fromNullable(connection.getProcess());
        this.process = processNullable1.isPresent() ? processNullable1.get().getId() : null;
        this.fromConnector = connection.getFromConnector();
        this.toConnector = connection.getToConnector();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFromTask() {
        return fromTask;
    }

    public void setFromTask(Long fromTask) {
        this.fromTask = fromTask;
    }

    public Long getToTask() {
        return toTask;
    }

    public void setToTask(Long toTask) {
        this.toTask = toTask;
    }

    public Long getProcess() {
        return process;
    }

    public void setProcess(Long process) {
        this.process = process;
    }

    public String getFromConnector() {
        return fromConnector;
    }

    public void setFromConnector(String fromConnector) {
        this.fromConnector = fromConnector;
    }

    public String getToConnector() {
        return toConnector;
    }

    public void setToConnector(String toConnector) {
        this.toConnector = toConnector;
    }
}
