package custom.domain;

import custom.dto.BaseDTO;
import custom.dto.TaskConnectionDTO;

import javax.persistence.*;

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
    private Task from;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "toId", nullable = false)
    private Task to;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "processId", nullable = false)
    private Process process;

    @Column(name = "fromConnector", nullable = false)
    private String fromConnector;

    @Column(name = "toConnector", nullable = false)
    private String toConnector;

    public TaskConnection() {
    }

    public TaskConnection(String name) {
        super(name);
    }

    public Task getFrom() {
        return from;
    }

    public void setFrom(Task from) {
        this.from = from;
    }

    public Task getTo() {
        return to;
    }

    public void setTo(Task to) {
        this.to = to;
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

    // TODO equals and hashcode
}
