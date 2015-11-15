package custom.domain;

import custom.dto.BaseDTO;
import custom.dto.TaskConnectionDTO;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "task_connections")
@NamedQueries({
        @NamedQuery(
                name = "custom.domain.TaskConnection.findAll",
                query = "SELECT tc FROM TaskConnection tc"
        )
})
public class TaskConnection extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "fromId", nullable = false)
    @NotNull
    private Task fromTask;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "toId", nullable = false)
    @NotNull
    private Task toTask;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "processId", nullable = false)
    @NotNull
    private Process process;

    @Column(name = "fromConnector", nullable = false)
    @NotBlank
    private String fromConnector;

    @Column(name = "toConnector", nullable = false)
    @NotBlank
    private String toConnector;

    public TaskConnection() {
    }

    public TaskConnection(String name) {
        super(name);
    }

    public Task getFromTask() {
        return fromTask;
    }

    public void setFromTask(Task fromTask) {
        this.fromTask = fromTask;
    }

    public Task getToTask() {
        return toTask;
    }

    public void setToTask(Task toTask) {
        this.toTask = toTask;
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

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public BaseDTO createDto() {
        return new TaskConnectionDTO(this);
    }

}
