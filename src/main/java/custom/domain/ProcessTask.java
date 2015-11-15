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
    private Long position;

    public ProcessTask() {
    }

    public ProcessTask(Process process, Task task, Long position) {
        this.process = process;
        this.task = task;
        this.position = position;
    }

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

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }
}
