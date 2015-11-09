package custom.domain;

import javax.persistence.*;

@Entity
@Table(name = "process_task")
@IdClass(ProcessTaskId.class)
public class ProcessTask {
    @Id
    @ManyToOne
    @JoinColumn(name = "processId", referencedColumnName = "id")
    private Process process;

    @Id
    @ManyToOne
    @JoinColumn(name = "taskId", referencedColumnName = "id")
    private Task task;

    @Column(name = "position")
    private long position;

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
