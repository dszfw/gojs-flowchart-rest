package com.example.helloworld.core;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "task_order")
public class TaskOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "taskId", nullable = false)
    private Task task;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "processId", nullable = false)
    private Process process;

    @Column(name = "position", nullable = false)
    private long position;

    public TaskOrder() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskOrder)) return false;

        TaskOrder taskOrder = (TaskOrder) o;

        if (getId() != taskOrder.getId()) return false;
        return getPosition() == taskOrder.getPosition();

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (int) (getPosition() ^ (getPosition() >>> 32));
        return result;
    }
}
